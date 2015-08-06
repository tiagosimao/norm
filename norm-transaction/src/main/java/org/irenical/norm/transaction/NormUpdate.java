package org.irenical.norm.transaction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NormUpdate<INPUT, OUTPUT> extends NTOperation<INPUT, OUTPUT> {

    @Override
    OUTPUT execute(NTContext<INPUT, OUTPUT> context) throws SQLException {
        try (PreparedStatement statement = JDBChops.prepareStatementForSelectOrUpdate(context.getConnection(), queryBuilder.apply(context.getInput()), parametersBuilder.apply(context.getInput()))) {
            int count = statement.executeUpdate();
            context.setPreparedStatement(statement);
            context.setUpdatedRows(count);
            return outputReader.toOutput(context);
        }
    }

}
