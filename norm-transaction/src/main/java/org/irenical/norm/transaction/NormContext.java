package org.irenical.norm.transaction;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NormContext<INPUT, OUTPUT> {

    private final NormTransaction<INPUT, OUTPUT> transaction;

    private NormOperation<INPUT, OUTPUT> operation;

    private Connection connection;

    private INPUT input;

    private OUTPUT output;

    private PreparedStatement preparedStatement;

    private CallableStatement callableStatement;

    private ResultSet resultset;

    private Integer updatedRows;

    private List<Map<String, Object>> generatedKeys;

    public NormContext(NormTransaction<INPUT, OUTPUT> nt) {
        this.transaction = nt;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public NormTransaction<INPUT, OUTPUT> getTransaction() {
        return transaction;
    }

    public void forward(NormOperation<INPUT, OUTPUT> nextOperation) {
        setCallableStatement(null);
        setPreparedStatement(null);
        setResultset(null);
        setUpdatedRows(null);
        generatedKeys = null;
        this.operation = nextOperation;
    }

    public NormOperation<INPUT, OUTPUT> getOperation() {
        return operation;
    }

    public void setPreviousOutput(OUTPUT output) {
        this.output = output;
    }

    public OUTPUT getPreviousOutput() {
        return output;
    }

    public void setInput(INPUT input) {
        this.input = input;
    }

    public INPUT getInput() {
        return input;
    }

    public void setCallableStatement(CallableStatement callableStatement) {
        this.callableStatement = callableStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public void setResultset(ResultSet resultset) {
        this.resultset = resultset;
    }

    public CallableStatement getCallableStatement() {
        return callableStatement;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public ResultSet getResultset() {
        return resultset;
    }

    public void setUpdatedRows(Integer updatedRows) {
        this.updatedRows = updatedRows;
    }

    public Integer getUpdatedRows() {
        return updatedRows;
    }

    public Integer getFirstGeneratedKeyAsInteger() throws SQLException {
        Object got = getFirstGeneratedKey();
        if(got instanceof Number){
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
        List<Map<String, Object>> generatedKeys = getGeneratedKeys();
        return generatedKeys.isEmpty() ? null : generatedKeys.get(0);
    }

    public List<Map<String, Object>> getGeneratedKeys() throws SQLException {
        loadGeneratedKeys();
        return generatedKeys;
    }

    private void loadGeneratedKeys() throws SQLException {
        if (generatedKeys == null) {
            List<Map<String, Object>> generatedKeys = new LinkedList<Map<String, Object>>();
            try (ResultSet resultset = preparedStatement.getGeneratedKeys()) {
                while (resultset.next()) {
                    Map<String, Object> row = new HashMap<>();
                    ResultSetMetaData metadata = resultset.getMetaData();
                    int columnCount = metadata.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metadata.getColumnName(i);
                        row.put(columnName, resultset.getObject(columnName));
                    }
                    generatedKeys.add(Collections.unmodifiableMap(row));
                }
            }
            this.generatedKeys = Collections.unmodifiableList(generatedKeys);
        }
    }

}
