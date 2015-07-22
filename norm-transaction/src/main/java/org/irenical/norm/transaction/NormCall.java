package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NormCall<INPUT> extends NormOperation<INPUT> {

    @Override
    void execute(Connection connection, INPUT a) throws SQLException {
        try (PreparedStatement statement = prepareStatementForCall(connection, queryBuilder.apply(a), parametersBuilder.apply(a))) {
            int count = statement.executeUpdate();
            NormResult<INPUT> result = new NormResult<>();
            result.setInput(a);
            result.setPreparedStatement(statement);
            result.setUpdatedRows(count);
            resultConsumer.accept(result);
        }
    }

}
