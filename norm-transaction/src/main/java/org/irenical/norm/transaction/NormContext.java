package org.irenical.norm.transaction;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class NormContext<INPUT, OUTPUT> {

  private NormTransaction<INPUT, OUTPUT> transaction;

  private Connection connection;

  private INPUT input;

  private OUTPUT output;

  private PreparedStatement preparedStatement;

  private CallableStatement callableStatement;

  private ResultSet resultSet;

  private Integer updatedRows;

  private Function<INPUT, ?> inputAdapter;
  
  protected NormContext() {
  }
  
  protected void setTransaction(NormTransaction<INPUT, OUTPUT> transaction) {
    this.transaction = transaction;
  }
  
  protected void setConnection(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return connection;
  }

  public NormTransaction<INPUT, OUTPUT> getTransaction() {
    return transaction;
  }

  protected void forward() {
    setCallableStatement(null);
    setPreparedStatement(null);
    setResultSet(null);
    setUpdatedRows(null);
  }

  protected void setCurrentOutput(OUTPUT output) {
    this.output = output;
  }

  public OUTPUT getCurrentOutput() {
    return output;
  }

  protected void setInput(INPUT input) {
    this.input = input;
  }

  @SuppressWarnings("unchecked")
  public INPUT getInput() {
    if(inputAdapter!=null){
      return (INPUT) inputAdapter.apply(input);
    }
    return input;
  }

  protected void setCallableStatement(CallableStatement callableStatement) {
    this.callableStatement = callableStatement;
  }

  protected void setPreparedStatement(PreparedStatement preparedStatement) {
    this.preparedStatement = preparedStatement;
  }

  protected void setResultSet(ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  public CallableStatement getCallableStatement() {
    return callableStatement;
  }

  public PreparedStatement getPreparedStatement() {
    return preparedStatement;
  }

  public ResultSet getResultSet() {
    return resultSet;
  }

  protected void setUpdatedRows(Integer updatedRows) {
    this.updatedRows = updatedRows;
  }

  public Integer getUpdatedRows() {
    return updatedRows;
  }

  public void setInputAdapter(Function<INPUT, ?> inputAdapter) {
    this.inputAdapter = inputAdapter;
  }

  public Integer getFirstGeneratedKeyAsInteger() throws SQLException {
    Object got = getFirstGeneratedKey();
    if (got instanceof Number) {
      return ((Number) got).intValue();
    } else {
      return Integer.valueOf(got.toString());
    }
  }

  public Object getFirstGeneratedKey() throws SQLException {
    Map<String, Object> firstRow = getFirstGeneratedKeys();
    return firstRow == null || firstRow.isEmpty() ? null : firstRow.entrySet().iterator().next().getValue();
  }

  public Map<String, Object> getFirstGeneratedKeys() throws SQLException {
    Iterator<Map<String, Object>> keysIterator = getGeneratedKeys().iterator();
    return keysIterator.hasNext() ? keysIterator.next() : Collections.emptyMap();
  }

  public Iterable<Map<String,Object>> getGeneratedKeys() throws SQLException {
    ResultSet generatedKeysResultSet = preparedStatement.getGeneratedKeys();

    return () -> new NormResultSetIterator(generatedKeysResultSet);
  }

}
