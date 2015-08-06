package org.irenical.norm.transaction;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

public class JDBChops {

    public static PreparedStatement prepareStatementForInsert(Connection connection, String query, Iterable<Object> parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        prepareInput(statement, parameters);
        return statement;
    }

    public static PreparedStatement prepareStatementForSelectOrUpdate(Connection connection, String query, Iterable<Object> parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        prepareInput(statement, parameters);
        return statement;
    }

    public static PreparedStatement prepareStatementForCall(Connection connection, String query, Iterable<Object> parameters) throws SQLException {
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

    public static void prepareInput(PreparedStatement preparedStatement, Iterable<Object> parameters) throws SQLException {
        if (parameters != null) {
            int idx = 0;
            for (Object param : parameters) {
                setInput(preparedStatement, ++idx, param);
            }
        }
    }

    public static void setInput(PreparedStatement preparedStatement, int idx, Object value) throws SQLException {
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

    public static void setOutput(CallableStatement preparedStatement, int idx, Object value) throws SQLException {
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
