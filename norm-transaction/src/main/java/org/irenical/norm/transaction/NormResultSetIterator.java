package org.irenical.norm.transaction;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/*package */class NormResultSetIterator implements Iterator<Map<String, Object>> {
  private ResultSet resultSet;
  private Map<String, Object> lastResult;
  private boolean hasNext;

  public NormResultSetIterator(ResultSet resultSet) {
    this.resultSet = resultSet;
    forwardResultSet();
  }

  @Override
  public boolean hasNext() {
    // use of isLast() requires scrollable resultSets which materialize results read in memory and/or disk
    return hasNext;
  }

  @Override
  public Map<String, Object> next() {
    Map<String, Object> result = lastResult;
    forwardResultSet();
    return result;
  }

  private void forwardResultSet() {
    try {
      hasNext = resultSet.next();
      lastResult = hasNext ? toRowMap(resultSet) : Collections.<String, Object>emptyMap();
    } catch (SQLException e) {
      hasNext = false;
      lastResult = Collections.<String, Object>emptyMap();
    }
  }

  private static Map<String, Object> toRowMap(ResultSet rs) throws SQLException {
    Map<String, Object> row = new LinkedHashMap<>();
    ResultSetMetaData metadata = rs.getMetaData();
    int columnCount = metadata.getColumnCount();
    for (int i = 1; i <= columnCount; i++) {
      String columnName = metadata.getColumnName(i);
      row.put(columnName, rs.getObject(columnName));
    }

    return row;
  }
}
