package org.irenical.norm.transaction;

import java.sql.Connection;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by tgsimao on 06/08/14.
 */

public class TransactionTest {
    
    @Test
    public void testCreateTransaction() {
        NormTransaction.create(()->Mockito.mock(Connection.class)); 
    }
    
    @Test
    public void testCreateSelect() {
        NormTransaction.create(()->Mockito.mock(Connection.class)).appendSelect((input)->"select * from students", null, null);
    }

}
