package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

import org.irenical.norm.transaction.error.NormTransactionException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TransactionTest {

    private static ConnectionSupplier connectionSupplier = () -> {
        return DriverManager.getConnection("jdbc:derby:memory:norm_testing;create=true");
    };

    private Function<Object, Iterable<Object>> inocuousParameterBuilder = (in) -> null;

    private NormResultConsumer<Object> resultConsumer = (out) -> Assert.assertNotNull(out);

    private static final NormResultConsumer<Object> resultSQLErrorConsumer = (out) -> {
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
    public void testCreateTransaction() {
        NormTransaction.create(connectionSupplier);
    }

    @Test
    public void testCreateSelect() {
        NormTransaction.create(connectionSupplier).appendSelect((in) -> "values 1", inocuousParameterBuilder, resultConsumer);
    }

    @Test
    public void testCreateInsertUpdateDelete() throws SQLException {
        final int [] genID = new int[1];
        NormTransaction<String> t = NormTransaction.create(connectionSupplier);
        t.appendInsert((in) -> "INSERT INTO PEOPLE (NAME) VALUES (?)", (in) -> Arrays.asList(in), (out) -> out.consumeGeneratedKeys((rs) -> {
            rs.next();
            genID[0]=rs.getInt(1);
        }));
        t.appendSelect((in) -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?", (in) -> Arrays.asList(in), (out) -> {
            ResultSet rs = out.getResultset();
            rs.next();
            Assert.assertEquals(genID[0], rs.getInt(1));
        });
        t.appendUpdate((in) -> "UPDATE PEOPLE SET NAME=? WHERE NAME=?", (in) -> Arrays.asList("Mr. " + in,in), (out) -> {
            Assert.assertEquals(1, (int) out.getUpdatedRows());
        });
        t.appendSelect((in) -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?", (in) -> Arrays.asList("Mr. " + in), (out) -> {
            ResultSet rs = out.getResultset();
            rs.next();
            Assert.assertEquals(genID[0], rs.getInt(1));
        });
        t.appendUpdate((in) -> "DELETE FROM PEOPLE WHERE NAME=?", (in) -> Arrays.asList(in), (out) -> {
            Assert.assertEquals(0, (int) out.getUpdatedRows());
        });
        t.appendUpdate((in) -> "DELETE FROM PEOPLE WHERE NAME=?", (in) -> Arrays.asList("Mr. " + in), (out) -> {
            Assert.assertEquals(1, (int) out.getUpdatedRows());
        });
        t.appendSelect((in) -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?", (in) -> Arrays.asList("Mr. " + in), (out) -> {
            ResultSet rs = out.getResultset();
            Assert.assertFalse(rs.next());
        });
        t.execute("Obama");
    }

    @Test
    public void testCreateCallable() {
        // TODO test procedure with IN parameters, OUT parameters, INOUT parameters and an unhealthy mix of them all
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoConnectionSupplier() throws SQLException {
        NormTransaction.create(null).appendSelect((in) -> "values 1", inocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoConnection() throws SQLException {
        NormTransaction.create(() -> null).appendSelect((in) -> "values 1", inocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoQueryBuilder() throws SQLException {
        NormTransaction.create(connectionSupplier).appendSelect(null, inocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = NormTransactionException.class)
    public void testSelectNoQuery() throws SQLException {
        NormTransaction.create(connectionSupplier).appendSelect((in) -> null, inocuousParameterBuilder, resultConsumer).execute();
    }

    @Test()
    public void testSelect() throws SQLException {
        NormTransaction.create(connectionSupplier).appendSelect((in) -> "values 1", inocuousParameterBuilder, resultConsumer).execute();
    }

    @Test()
    public void testSelectNoParameterBuilder() throws SQLException {
        NormTransaction.create(connectionSupplier).appendSelect((in) -> "values 1", null, resultConsumer).execute();
    }

    @Test()
    public void testSelectNoParameters() throws SQLException {
        NormTransaction.create(connectionSupplier).appendSelect((in) -> "values 1", (in) -> null, resultConsumer).execute();
    }

    @Test(expected = SQLException.class)
    public void testSelectSQLErrorSyntax() throws SQLException {
        NormTransaction.create(connectionSupplier).appendSelect((in) -> "select your_mom", inocuousParameterBuilder, resultConsumer).execute();
    }

    @Test(expected = SQLException.class)
    public void testSelectSQLErrorOnRead() throws SQLException {
        NormTransaction.create(connectionSupplier).appendSelect((in) -> "values 1", inocuousParameterBuilder, resultSQLErrorConsumer).execute();
    }

}
