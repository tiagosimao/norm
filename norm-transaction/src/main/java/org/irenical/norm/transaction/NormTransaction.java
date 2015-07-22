package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.irenical.norm.transaction.error.NormTransactionException;

public class NormTransaction<INPUT> {

    private ConnectionSupplier connectionSupplier;

    private final List<NormOperation<INPUT>> operations = new LinkedList<>();

    private NormTransaction() {
    }

    public static <INPUT> NormTransaction<INPUT> create(ConnectionSupplier connectionSupplier) {
        NormTransaction<INPUT> result = new NormTransaction<>();
        result.setConnectionSupplier(connectionSupplier);
        return result;
    }

    public void setConnectionSupplier(ConnectionSupplier connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    public NormTransaction<INPUT> appendSelect(Function<INPUT, String> queryBuilder, Function<INPUT, Iterable<Object>> parametersBuilder, ResultConsumer<INPUT> resultConsumer) {
        NormSelect<INPUT> select = new NormSelect<>();
        select.setQueryBuilder(queryBuilder);
        select.setParametersBuilder(parametersBuilder);
        select.setResultConsumer(resultConsumer);
        operations.add(select);
        return this;
    }

    public NormTransaction<INPUT> appendInsert(Function<INPUT, String> queryBuilder, Function<INPUT, Iterable<Object>> parametersBuilder, ResultConsumer<INPUT> resultConsumer) {
        NormInsert<INPUT> insert = new NormInsert<>();
        insert.setQueryBuilder(queryBuilder);
        insert.setParametersBuilder(parametersBuilder);
        insert.setResultConsumer(resultConsumer);
        operations.add(insert);
        return this;
    }

    public NormTransaction<INPUT> appendUpdate(Function<INPUT, String> queryBuilder, Function<INPUT, Iterable<Object>> parametersBuilder, ResultConsumer<INPUT> resultConsumer) {
        NormUpdate<INPUT> update = new NormUpdate<>();
        update.setQueryBuilder(queryBuilder);
        update.setParametersBuilder(parametersBuilder);
        update.setResultConsumer(resultConsumer);
        operations.add(update);
        return this;
    }

    public NormTransaction<INPUT> appendDelete(Function<INPUT, String> queryBuilder, Function<INPUT, Iterable<Object>> parametersBuilder, ResultConsumer<INPUT> resultConsumer) {
        NormUpdate<INPUT> update = new NormUpdate<>();
        update.setQueryBuilder(queryBuilder);
        update.setParametersBuilder(parametersBuilder);
        update.setResultConsumer(resultConsumer);
        operations.add(update);
        return this;
    }

    public NormTransaction<INPUT> appendCallable(Function<INPUT, String> queryBuilder, Function<INPUT, Iterable<Object>> parametersBuilder, ResultConsumer<INPUT> resultConsumer) {
        NormCall<INPUT> call = new NormCall<>();
        call.setQueryBuilder(queryBuilder);
        call.setParametersBuilder(parametersBuilder);
        call.setResultConsumer(resultConsumer);
        operations.add(call);
        return this;
    }

    public void execute() throws SQLException {
        execute(null);
    }

    public void execute(INPUT a) throws SQLException {
        if (connectionSupplier == null) {
            throw new NormTransactionException("No connection supplier for this transaction");
        }
        Connection connection = null;
        connection = connectionSupplier.get();
        if (connection == null) {
            throw new NormTransactionException("Null connection supplied to this transaction");
        }
        try {
            for (NormOperation<INPUT> operation : operations) {
                operation.execute(connection, a);
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
            }
        }
    }

}
