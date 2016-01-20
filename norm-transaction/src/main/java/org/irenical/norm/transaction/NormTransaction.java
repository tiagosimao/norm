package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.irenical.norm.transaction.error.NormTransactionException;

public class NormTransaction<INPUT, OUTPUT> {

    private final List<NTOperation<INPUT, OUTPUT>> operations = new LinkedList<>();

    private NTHook hook;

    private NTConnectionSupplier connectionSupplier;

    public NormTransaction() {
        this(null);
    }

    public NormTransaction(NTConnectionSupplier connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    public void setConnectionSupplier(NTConnectionSupplier connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    public NTConnectionSupplier getConnectionSupplier() {
        return connectionSupplier;
    }

    public void setHook(NTHook hook) {
        this.hook = hook;
    }

    public NTHook getHook() {
        return hook;
    }
    
    public NormTransaction<INPUT, OUTPUT> appendSelect(Function<NTContext<INPUT, OUTPUT>, String> queryBuilder,
            Function<NTContext<INPUT, OUTPUT>, Iterable<Object>> parametersBuilder, NTOutputReader<INPUT, OUTPUT> resultConsumer) {
        NormSelect<INPUT, OUTPUT> select = new NormSelect<>();
        select.setQueryBuilder(queryBuilder);
        select.setParametersBuilder(parametersBuilder);
        select.setResultConsumer(resultConsumer);
        return appendSelect(select);
    }

    public NormTransaction<INPUT, OUTPUT> appendSelect(NormSelect<INPUT, OUTPUT> select) {
        operations.add(select);
        return this;
    }

    public NormTransaction<INPUT, OUTPUT> appendInsert(Function<NTContext<INPUT, OUTPUT>, String> queryBuilder,
            Function<NTContext<INPUT, OUTPUT>, Iterable<Object>> parametersBuilder, NTOutputReader<INPUT, OUTPUT> resultConsumer) {
        NormInsert<INPUT, OUTPUT> insert = new NormInsert<>();
        insert.setQueryBuilder(queryBuilder);
        insert.setParametersBuilder(parametersBuilder);
        insert.setResultConsumer(resultConsumer);
        return appendInsert(insert);
    }

    public NormTransaction<INPUT, OUTPUT> appendInsert(NormInsert<INPUT, OUTPUT> insert) {
        operations.add(insert);
        return this;
    }

    public NormTransaction<INPUT, OUTPUT> appendUpdate(Function<NTContext<INPUT, OUTPUT>, String> queryBuilder,
            Function<NTContext<INPUT, OUTPUT>, Iterable<Object>> parametersBuilder, NTOutputReader<INPUT, OUTPUT> resultConsumer) {
        NormUpdate<INPUT, OUTPUT> update = new NormUpdate<>();
        update.setQueryBuilder(queryBuilder);
        update.setParametersBuilder(parametersBuilder);
        update.setResultConsumer(resultConsumer);
        return appendUpdate(update);
    }

    public NormTransaction<INPUT, OUTPUT> appendUpdate(NormUpdate<INPUT, OUTPUT> update) {
        operations.add(update);
        return this;
    }

    public NormTransaction<INPUT, OUTPUT> appendDelete(Function<NTContext<INPUT, OUTPUT>, String> queryBuilder,
            Function<NTContext<INPUT, OUTPUT>, Iterable<Object>> parametersBuilder, NTOutputReader<INPUT, OUTPUT> resultConsumer) {
        NormDelete<INPUT, OUTPUT> delete = new NormDelete<>();
        delete.setQueryBuilder(queryBuilder);
        delete.setParametersBuilder(parametersBuilder);
        delete.setResultConsumer(resultConsumer);
        return appendDelete(delete);
    }

    public NormTransaction<INPUT, OUTPUT> appendDelete(NormUpdate<INPUT, OUTPUT> delete) {
        operations.add(delete);
        return this;
    }

    public NormTransaction<INPUT, OUTPUT> appendCallable(Function<NTContext<INPUT, OUTPUT>, String> queryBuilder,
            Function<NTContext<INPUT, OUTPUT>, Iterable<Object>> parametersBuilder, NTOutputReader<INPUT, OUTPUT> resultConsumer) {
        NormCall<INPUT, OUTPUT> call = new NormCall<>();
        call.setQueryBuilder(queryBuilder);
        call.setParametersBuilder(parametersBuilder);
        call.setResultConsumer(resultConsumer);
        return appendCallable(call);
    }

    public NormTransaction<INPUT, OUTPUT> appendCallable(NormCall<INPUT, OUTPUT> call) {
        operations.add(call);
        return this;
    }

    public OUTPUT execute() throws SQLException {
        return execute(connectionSupplier, null);
    }

    public OUTPUT execute(INPUT input) throws SQLException {
        return execute(connectionSupplier, input);
    }

    public OUTPUT execute(NTConnectionSupplier connectionSupplier, INPUT input) throws SQLException {
        if (connectionSupplier == null) {
            throw new NormTransactionException("No connection supplier was provided for this transaction");
        }
        NTContext<INPUT, OUTPUT> context = new NTContext<>(this);
        context.setInput(input);
        if (hook != null) {
            hook.transactionStarted(context);
        }
        Connection connection = null;
        connection = connectionSupplier.get();
        if (connection == null) {
            throw new NormTransactionException("Null connection supplied to this transaction");
        }
        context.setConnection(connection);
        try {
            for (NTOperation<INPUT, OUTPUT> operation : operations) {

                context.forward(operation);

                if (hook != null) {
                    hook.operationStarted(context);
                }
                context.setPreviousOutput(operation.execute(context));
                if (hook != null) {
                    hook.operationEnded(context);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                // Error while rolling back... ignoring.
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
                if (hook != null) {
                    hook.transactionEnded(context);
                }
            }
        }
        return context.getPreviousOutput();
    }
    
}
