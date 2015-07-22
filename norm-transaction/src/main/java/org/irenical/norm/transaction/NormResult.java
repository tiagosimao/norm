package org.irenical.norm.transaction;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NormResult<INPUT> {
    
    private INPUT input;
    
    private PreparedStatement preparedStatement;
    
    private CallableStatement callableStatement;
    
    private ResultSet resultset;
    
    private Integer updatedRows;
    
    public void setInput(INPUT input) {
        this.input = input;
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
    
    public INPUT getInput() {
        return input;
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
    
    public void consumeGeneratedKeys(ResultSetConsumer generatedKeysConsumer) throws SQLException {
        try(ResultSet resultset = preparedStatement.getGeneratedKeys()){
            generatedKeysConsumer.accept(resultset);
        }
    }
    
}
