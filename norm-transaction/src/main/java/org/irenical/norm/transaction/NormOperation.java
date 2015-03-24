package org.irenical.norm.transaction;

import java.sql.Connection;
import java.util.Iterator;
import java.util.function.Consumer;

import org.irenical.norm.NormResult;
import org.irenical.norm.NormRow;

public interface NormOperation extends Iterator<NormOperation> {
    
    public Consumer<NormRow> getConsumer();
    
    @Override
    public boolean hasNext();
    
    @Override
    public NormOperation next();
    
    public NormResult execute(Connection connection) throws NormExecuteOperationException;
    
    public void close() throws NormCloseException;
    
}
