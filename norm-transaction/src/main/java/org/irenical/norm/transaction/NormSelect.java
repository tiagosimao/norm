package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NormSelect<INPUT> extends NormOperation<INPUT> {

    @Override
    void execute(Connection connection, INPUT a) throws SQLException {
        try (PreparedStatement statement = prepareStatementForSelectOrUpdate(connection, queryBuilder.apply(a), parametersBuilder.apply(a))) {
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
