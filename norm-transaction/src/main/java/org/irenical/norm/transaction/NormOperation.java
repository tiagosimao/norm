package org.irenical.norm.transaction;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class NormOperation<INPUT> {

    protected Function<INPUT, String> queryBuilder;

    protected Function<INPUT, Iterable<Object>> parametersBuilder;

    protected Consumer<NormResult<INPUT>> resultConsumer;

    abstract void execute(Connection connection, INPUT a) throws SQLException;

    public void setParametersBuilder(Function<INPUT, Iterable<Object>> parametersBuilder) {
        this.parametersBuilder = parametersBuilder;
    }

    public void setQueryBuilder(Function<INPUT, String> queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public Function<INPUT, Iterable<Object>> getParametersBuilder() {
        return parametersBuilder;
    }

    public Function<INPUT, String> getQueryBuilder() {
        return queryBuilder;
    }

    public void setResultConsumer(Consumer<NormResult<INPUT>> resultConsumer) {
        this.resultConsumer = resultConsumer;
    }

    public Consumer<NormResult<INPUT>> getResultConsumer() {
        return resultConsumer;
    }

    protected PreparedStatement prepareStatementForInsert(Connection connection, String query, Iterable<Object> parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        prepareInput(statement, parameters);
        return statement;
    }

    protected PreparedStatement prepareStatementForSelectOrUpdate(Connection connection, String query, Iterable<Object> parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        prepareInput(statement, parameters);
        return statement;
    }

    protected PreparedStatement prepareStatementForCall(Connection connection, String query, Iterable<Object> parameters) throws SQLException {
        CallableStatement statement = connection.prepareCall(query);
        if (parameters != null) {
            int idx = 0;
            for (Object parameter : parameters) {
                if (parameter instanceof NormOutputParameterWrapper) {
                    setOutput(statement, ++idx, ((NormOutputParameterWrapper) parameter).getValue());
                } else {
                    setInput(statement, ++idx, parameter);
                }
            }
        }
        return statement;
    }

    private static void prepareInput(PreparedStatement preparedStatement, Iterable<Object> parameters) throws SQLException {
        if (parameters != null) {
            int idx = 0;
            for (Object param : parameters) {
                setInput(preparedStatement, ++idx, param);
            }
        }
    }

    private static void setInput(PreparedStatement preparedStatement, int idx, Object value) throws SQLException {
        if (value instanceof Timestamp) {
            preparedStatement.setTimestamp(idx, (Timestamp) value);
        } else if (value instanceof Time) {
            preparedStatement.setTime(idx, (Time) value);
        } else if (value instanceof Date) {
            preparedStatement.setDate(idx, (Date) value);
        } else if (value instanceof Enum<?>) {
            preparedStatement.setString(idx, value.toString());
        } else {
            preparedStatement.setObject(idx, value);
        }
    }

    private static void setOutput(CallableStatement preparedStatement, int idx, Object value) throws SQLException {
        if (value instanceof String) {
            preparedStatement.registerOutParameter(idx, java.sql.Types.VARCHAR);
        } else if (value instanceof Float) {
            preparedStatement.registerOutParameter(idx, java.sql.Types.FLOAT);
        } else if (value instanceof Integer) {
            preparedStatement.registerOutParameter(idx, java.sql.Types.INTEGER);
        } else if (value instanceof Timestamp) {
            preparedStatement.registerOutParameter(idx, java.sql.Types.TIMESTAMP);
        } else if (value instanceof Boolean) {
            preparedStatement.registerOutParameter(idx, java.sql.Types.BOOLEAN);
        } else {
            preparedStatement.registerOutParameter(idx, java.sql.Types.JAVA_OBJECT);
        }
    }

}
