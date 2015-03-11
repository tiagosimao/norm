package org.irenical.norm.transaction;

import org.irenical.norm.transaction.error.NormTransactionBeginException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by tgsimao on 30/07/14.
 */
public abstract class NormTransaction<INPUT, OUTPUT, ERROR extends Exception> {

    protected abstract Connection begin() throws NormTransactionBeginException;

    protected abstract OUTPUT run(Connection connection, INPUT input) throws SQLException, ERROR;

    @SuppressWarnings("unchecked")
	public OUTPUT execute(INPUT input) throws SQLException, ERROR {
        OUTPUT result = null;
        Connection connection = begin();
        if (connection == null) {
            throw new NormTransactionBeginException("Got null connection from begin() method");
        }
        Exception caught = null;
        try {
            result = run(connection, input);
            connection.commit();
        } catch (Exception e) {
            caught = e;
            connection.rollback();
        } finally {
            connection.close();
            if (caught instanceof RuntimeException) {
                throw (RuntimeException) caught;
            } else if (caught != null) {
                if(caught instanceof SQLException){
                    throw (SQLException) caught;
                } else {
                    throw (ERROR) caught;
                }
            }
        }
        return result;
    }

}
