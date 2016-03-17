package org.irenical.norm.transaction;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NormCall<INPUT, OUTPUT> extends NormOperation<INPUT, OUTPUT> {

  @Override
  OUTPUT execute(NormContext<INPUT, OUTPUT> context) throws SQLException {
    try (CallableStatement statement = JDBChops.prepareStatementForCall(context.getConnection(),
            queryBuilder.apply(context),
            parametersBuilder == null ? null : parametersBuilder.apply(context))
    ) {
      boolean hasResultSet = statement.execute();

      context.setCallableStatement(statement);
      context.setResultSet(hasResultSet ? statement.getResultSet() : null);

      if (outputReader != null) {
        return outputReader.toOutput(context);
      }
      return null;
    }
  }

}
