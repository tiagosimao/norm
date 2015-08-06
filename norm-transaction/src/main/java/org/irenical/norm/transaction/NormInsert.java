package org.irenical.norm.transaction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NormInsert<INPUT, OUTPUT> extends NTOperation<INPUT, OUTPUT> {

    @Override
    OUTPUT execute(NTContext<INPUT, OUTPUT> context) throws SQLException {
        try (PreparedStatement statement = JDBChops.prepareStatementForInsert(context.getConnection(), queryBuilder.apply(context.getInput()), parametersBuilder.apply(context.getInput()))) {
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
