package org.irenical.norm.transaction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NormInsert<INPUT, OUTPUT> extends NormOperation<INPUT, OUTPUT> {

    @Override
    OUTPUT execute(NormContext<INPUT, OUTPUT> context) throws SQLException {
        try (PreparedStatement statement = JDBChops.prepareStatementForInsert(context.getConnection(), queryBuilder.apply(context), parametersBuilder == null ? null : parametersBuilder.apply(context))) {
            int count = statement.executeUpdate();
            context.setPreparedStatement(statement);
            context.setUpdatedRows(count);
            if (outputReader != null) {
                return outputReader.toOutput(context);
            }
            return null;
        }
    }

}
