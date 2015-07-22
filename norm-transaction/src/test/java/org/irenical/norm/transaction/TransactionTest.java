package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

import org.irenical.norm.transaction.error.NormTransactionException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class TransactionTest {

    private static final ConnectionSupplier mockConnectionSupplier = () -> Mockito.mock(Connection.class);

    private static final Function<Object, String> inocuousQueryBuilder = (in) -> "some SQL";

    private static final Function<Object, Iterable<Object>> inocuousParameterBuilder = (in) -> null;

    private static final ResultConsumer<Object> resultConsumer = (out) -> Assert.assertNotNull(out);

    private static final ResultConsumer<Object> resultSQLErrorConsumer = (out) -> {
        throw new SQLException();
    };

    @Test
    public void testCreateTransaction() {
        NormTransaction.create(mockConnectionSupplier);
    }

    @Test
    public void testCreateSelect() {
        NormTransaction.create(mockConnectionSupplier).appendSelect(inocuousQueryBuilder, inocuousParameterBuilder, resultConsumer);
    }

    @Test
    public void testCreateInsert() {
        NormTransaction.create(mockConnectionSupplier).appendInsert(inocuousQueryBuilder, inocuousParameterBuilder, resultConsumer);
    }

    @Test
    public void testCreateUpdate() {
        NormTransaction.create(mockConnectionSupplier).appendUpdate(inocuousQueryBuilder, inocuousParameterBuilder, resultConsumer);
    }

    @Test
    public void testCreateDelete() {
        NormTransaction.create(mockConnectionSupplier).appendDelete(inocuousQueryBuilder, inocuousParameterBuilder, resultConsumer);
    }

    @Test
    public void testCreateCallable() {
        NormTransaction.create(mockConnectionSupplier).appendCallable(inocuousQueryBuilder, inocuousParameterBuilder, resultConsumer);
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoConnectionSupplier() throws SQLException {
        NormTransaction.create(null).appendSelect(inocuousQueryBuilder, inocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoConnection() throws SQLException {
        NormTransaction.create(() -> null).appendSelect(inocuousQueryBuilder, inocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoQueryBuilder() throws SQLException {
        NormTransaction.create(mockConnectionSupplier).appendSelect(null, inocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoQuery() throws SQLException {
        NormTransaction.create(mockConnectionSupplier).appendSelect((in) -> null, inocuousParameterBuilder, resultConsumer).execute();
    }

//    @Test()
//    public void testSelect() throws SQLException {
//        NormTransaction.create(mockConnectionSupplier).appendSelect(inocuousQueryBuilder, inocuousParameterBuilder, resultConsumer).execute();
//    }

//    @Test()
//    public void testSelectNoParameterBuilder() throws SQLException {
//        NormTransaction.create(mockConnectionSupplier).appendSelect(inocuousQueryBuilder, null, resultConsumer).execute();
//    }

//    @Test()
//    public void testSelectNoParameters() throws SQLException {
//        NormTransaction.create(mockConnectionSupplier).appendSelect(inocuousQueryBuilder, (in) -> null, resultConsumer).execute();
//    }

//    @Test(expected = SQLException.class)
//    public void testSelectSQLError() throws SQLException {
//        NormTransaction.create(mockConnectionSupplier).appendSelect(inocuousQueryBuilder, inocuousParameterBuilder, resultSQLErrorConsumer).execute();
//    }

}
