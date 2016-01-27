package org.irenical.norm.transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.irenical.norm.transaction.error.NormTransactionException;

public class NormSelect<INPUT, OUTPUT> extends NormOperation<INPUT, OUTPUT> {

  @Override
  OUTPUT execute(NormContext<INPUT, OUTPUT> context) throws SQLException {
    if (queryBuilder == null) {
      throw new NormTransactionException("No query builder was provided for this select operation " + this);
    }
    String query = queryBuilder.apply(context);
    if (query == null || query.isEmpty()) {
      throw new NormTransactionException("A null or empty query was provided for this select operation " + this);
    }
    try (PreparedStatement statement = JDBChops.prepareStatementForSelectOrUpdate(context.getConnection(), query, parametersBuilder == null ? null : parametersBuilder.apply(context))) {
      try (ResultSet resultSet = statement.executeQuery()) {
        context.setPreparedStatement(statement);
        context.setResultset(resultSet);
        if (outputReader != null) {
          return outputReader.toOutput(context);
        }
        return null;
      }
    }
  }

}
