package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Function;

import org.irenical.norm.transaction.error.NormTransactionException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TransactionTest {

    private static NTConnectionSupplier connectionSupplier = () -> DriverManager.getConnection("jdbc:derby:memory:norm_testing;create=true");

    private Function<NTContext<Object, Object>, Iterable<Object>> innocuousParameterBuilder = context -> null;

    private NTOutputReader<Object, Object> resultConsumer = (context) -> {
        Assert.assertNotNull(context.getResultset());
        return null;
    };

    private static final NTOutputReader<Object, Object> resultSQLErrorConsumer = (context) -> {
        throw new SQLException();
    };

    @BeforeClass
    public static void init() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Connection connection = connectionSupplier.get();

        // create table
        PreparedStatement createPeopleTableStatement = connection.prepareStatement("CREATE TABLE PEOPLE (PERSON_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT PEOPLE_PK PRIMARY KEY, NAME VARCHAR(26))");
        createPeopleTableStatement.executeUpdate();
        createPeopleTableStatement.close();

        // create data
        PreparedStatement createPersonStatement = connection.prepareStatement("INSERT INTO PEOPLE (NAME) VALUES ('Boda')");
        createPersonStatement.executeUpdate();
        createPersonStatement.close();

        connection.close();
    }

    @Test
    public void testCreateSelect() throws SQLException {
        NormTransaction<Object, Object> transaction = new NormTransaction<>(connectionSupplier);
        transaction.appendSelect(context -> "values 1", innocuousParameterBuilder, resultConsumer);
        transaction.execute();
    }

    @Test
     public void testNullOutputBuilder() throws SQLException {
        NormTransaction<Object, Object> transaction = new NormTransaction<>(connectionSupplier);
        transaction.appendSelect(context -> "values 1", innocuousParameterBuilder, null);
        transaction.appendUpdate(context -> "update people set name = '42' where false", innocuousParameterBuilder, null);
        transaction.appendDelete(context -> "delete from people where false", innocuousParameterBuilder, null);
        transaction.appendInsert(context -> "insert into people(name) (select '42' from people where false)", innocuousParameterBuilder, null);
        transaction.execute();
    }

    @Test
    public void testCreateInsertUpdateDelete() throws SQLException {
        NormTransaction<String, Integer> t = new NormTransaction<>(connectionSupplier);

        t.appendInsert(context -> "INSERT INTO PEOPLE (NAME) VALUES (?)",
                context -> Arrays.asList(context.getInput()),
                NTContext::getFirstGeneratedKeyAsInteger);

        t.appendSelect(context -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?",
                context -> Arrays.asList(context.getInput()),
                context -> {
                    ResultSet rs = context.getResultset();
                    Assert.assertTrue(rs.next());
                    Assert.assertEquals(context.getPreviousOutput(), (Integer) rs.getInt(1));
                    return context.getPreviousOutput();
                });

        t.appendUpdate(context -> "UPDATE PEOPLE SET NAME=? WHERE NAME=?",
                context -> Arrays.asList("Mr. " + context.getInput(), context.getInput()),
                context -> {
                    Assert.assertEquals(1, (int) context.getUpdatedRows());
                    return context.getPreviousOutput();
                });

        t.appendSelect(context -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?",
                context -> Arrays.asList("Mr. " + context.getInput()),
                context -> {
                    ResultSet rs = context.getResultset();
                    rs.next();
                    Assert.assertEquals(context.getPreviousOutput(), (Integer) rs.getInt(1));
                    return context.getPreviousOutput();
                });

        t.appendDelete(context -> "DELETE FROM PEOPLE WHERE NAME=?",
                context -> Arrays.asList(context.getInput()),
                context -> {
                    Assert.assertEquals(0, (int) context.getUpdatedRows());
                    return context.getPreviousOutput();
                });

        t.appendUpdate(context -> "DELETE FROM PEOPLE WHERE NAME=?",
                context -> Arrays.asList("Mr. " + context.getInput()),
                context -> {
                    Assert.assertEquals(1, (int) context.getUpdatedRows());
                    return context.getPreviousOutput();
                });

        t.appendSelect(context -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?",
                context -> Arrays.asList("Mr. " + context.getInput()),
                context -> {
                    ResultSet rs = context.getResultset();
                    Assert.assertFalse(rs.next());
                    return context.getPreviousOutput();
                });

        t.execute("Obama");
    }

    @Test
    public void testCreateCallable() {
        // TODO test procedure with IN parameters, OUT parameters, INOUT
        // parameters and an unhealthy mix of them all
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoConnectionSupplier() throws SQLException {
        new NormTransaction<>().appendSelect(context -> "values 1", innocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoConnection() throws SQLException {
        new NormTransaction<>(() -> null).appendSelect(context -> "values 1", innocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoQueryBuilder() throws SQLException {
        NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
        t.appendSelect(null, innocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoQuery() throws SQLException {
        NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
        t.appendSelect(context -> null, innocuousParameterBuilder, resultConsumer).execute();
    }

    @Test()
    public void testSelect() throws SQLException {
        NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
        t.appendSelect(context -> "values 1", innocuousParameterBuilder, resultConsumer).execute();
    }

    @Test()
    public void testSelectNoParameterBuilder() throws SQLException {
        NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
        t.appendSelect(context -> "values 1", null, resultConsumer).execute();
    }

    @Test()
    public void testSelectNoParameters() throws SQLException {
        NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
        t.appendSelect(context -> "values 1", (in) -> null, resultConsumer).execute();
    }

    @Test(expected = SQLException.class)
    public void testSelectSQLErrorSyntax() throws SQLException {
        NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
        t.appendSelect(context -> "select your_mom", innocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = SQLException.class)
    public void testSelectSQLErrorOnRead() throws SQLException {
        NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
        t.appendSelect(context -> "values 1", innocuousParameterBuilder, resultSQLErrorConsumer).execute();
    }

}
