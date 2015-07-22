package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.irenical.norm.transaction.error.NormTransactionException;

public class NormSelect<INPUT> extends NormOperation<INPUT> {

    @Override
    void execute(Connection connection, INPUT a) throws SQLException {
        if (connection == null) {
            throw new NormTransactionException("No connection for this select operation " + this);
        }
        if (queryBuilder == null) {
            throw new NormTransactionException("No query builder for this select operation " + this);
        }
        String query = queryBuilder.apply(a);
        if (query == null || query.isEmpty()) {
            throw new NormTransactionException("Null or empty query for this select operation " + this);
        }
        try (PreparedStatement statement = prepareStatementForSelectOrUpdate(connection, query, parametersBuilder == null ? null : parametersBuilder.apply(a))) {
            try (ResultSet resultSet = statement.executeQuery()) {
                NormResult<INPUT> result = new NormResult<>();
                result.setInput(a);
                result.setPreparedStatement(statement);
                result.setResultset(resultSet);
                resultConsumer.accept(result);
            }
        }
    }

}
