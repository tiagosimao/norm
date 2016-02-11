package org.irenical.norm.transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NormCall <INPUT, OUTPUT> extends NormOperation<INPUT, OUTPUT> {

    @Override
    OUTPUT execute(NormContext<INPUT, OUTPUT> context) throws SQLException {
        try (PreparedStatement statement = JDBChops.prepareStatementForCall(context.getConnection(), queryBuilder.apply(context), parametersBuilder == null ? null : parametersBuilder.apply(context))) {
            ResultSet rs = statement.executeQuery();
            context.setPreparedStatement(statement);
            context.setResultSet(rs);
            if (outputReader != null) {
                return outputReader.toOutput(context);
            }
            return null;
        }
    }

}
