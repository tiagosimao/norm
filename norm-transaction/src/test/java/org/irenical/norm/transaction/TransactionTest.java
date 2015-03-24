package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import org.irenical.norm.NormResult;
import org.irenical.norm.transaction.error.NormTransactionBeginException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by tgsimao on 06/08/14.
 */

public class TransactionTest {

    private Connection connection;
    
    private void readRow(NormResult row){
    	System.out.println(row);
    }
    
    @Before
    public void malloc() throws SQLException{
        connection = Mockito.mock(Connection.class);
        
        
        
        
        
//        
//        noConnectionTransaction = new NormTransaction<Void, Void, NormTestException>() {
//            @Override
//            protected Connection begin() throws NormTransactionBeginException {
//                return null;
//            }
//
//            @Override
//            protected Void run(Connection connection, Void o) throws SQLException, NormTestException {
//                return null;
//            }
//        };
//        emptyTransaction = new NormTransaction<Void, Void, NormTestException>() {
//            @Override
//            protected Connection begin() throws NormTransactionBeginException {
//                return connection;
//            }
//
//            @Override
//            protected Void run(Connection connection, Void o) throws SQLException, NormTestException {
//                return null;
//            }
//        };
//        trivialTransaction = new NormTransaction<Integer, Integer, NormTestException>() {
//            @Override
//            protected Connection begin() throws NormTransactionBeginException {
//                return connection;
//            }
//
//            @Override
//            protected Integer run(Connection connection, Integer in) throws SQLException, NormTestException {
//                return in + 1;
//            }
//        };
//        
//        
//        
//        slowTransaction = new NormTransaction<Integer, Integer, InterruptedException>() {
//            @Override
//            protected Connection begin() throws NormTransactionBeginException {
//                return connection;
//            }
//
//            @Override
//            protected Integer run(Connection connection, Integer in) throws SQLException, InterruptedException {
//                Thread.sleep(1000 + (int)(1000 * Math.random()));
//                return in + 1;
//            }
//        };
//        faultyTransaction1 = new NormTransaction<Void, Void, NormTestException>() {
//            @Override
//            protected Connection begin() throws NormTransactionBeginException {
//                return connection;
//            }
//
//            @Override
//            protected Void run(Connection connection, Void o) throws SQLException, NormTestException {
//                throw new NormTestException();
//            }
//        };
//        faultyTransaction2 = new NormTransaction<Void, Void, NormTestException>() {
//            @Override
//            protected Connection begin() throws NormTransactionBeginException {
//                return connection;
//            }
//
//            @Override
//            protected Void run(Connection connection, Void o) throws SQLException, NormTestException {
//                throw new NormTestRuntimeException();
//            }
//        };
    }

    @After
    public void free(){
        connection = null;
//        noConnectionTransaction = null;
//        emptyTransaction = null;
//        trivialTransaction = null;
//        slowTransaction = null;
//        faultyTransaction1 = null;
//        faultyTransaction2 = null;
    }

    @Test
    public void stream() {
//    	try{
//    		Norm.begin(()->connection).select("select 1").stream().forEachOrdered(this::readRow);
//    	} catch(Exception e){
//    		e.printStackTrace();
//    	}
    }

//    @Test(expected=NormTransactionBeginException.class)
//    public void noConnection() throws SQLException, NormTestException {
//        noConnectionTransaction.execute(null);
//    }
//
//    @Test
//    public void nothingToDo() throws SQLException, NormTestException {
//        Assert.assertNull(emptyTransaction.execute(null));
//    }
//
//    @Test
//    public void littleToDo() throws SQLException, NormTestException {
//        Assert.assertEquals((Object)2,(Object)trivialTransaction.execute(1));
//    }
//
//    @Test
//    public void slowTransaction() throws SQLException, InterruptedException {
//        Assert.assertEquals((Object)2,(Object)slowTransaction.execute(1));
//    }
//
//    @Test(expected=NormTestException.class)
//    public void declaredException() throws SQLException, NormTestException {
//        faultyTransaction1.execute(null);
//    }
//
//    @Test(expected=NormTestRuntimeException.class)
//    public void runtimeException() throws SQLException, NormTestException {
//        faultyTransaction2.execute(null);
//    }

}
