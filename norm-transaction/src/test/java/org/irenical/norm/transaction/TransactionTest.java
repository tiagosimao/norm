
package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import org.irenical.norm.transaction.error.NormTransactionException;
import org.irenical.norm.transaction.error.TestSQLException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TransactionTest {

  private static NormConnectionSupplier connectionSupplier = () -> DriverManager.getConnection("jdbc:derby:memory:norm_testing;create=true");

  private static final String INNOCUOUS_SELECT = "values 1";

  private Function<NormContext<Object, Object>, Boolean> innocuousCondition = context -> true;
  private Function<NormContext<Object, Object>, Iterable<Object>> innocuousParameterBuilder = context -> new ArrayList<>();
  private NormOutputReader<Object, Object> innocuousOutputReader = context -> null;
  private NormOutputReader<Object, Object> assertNotNullOutputReader = context -> {
    Assert.assertNotNull(context.getResultSet());
    return null;
  };
  private NormOutputReader<Object, Object> failOutputReader = context -> {
    throw new TestSQLException();
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
    transaction.appendSelect(context -> INNOCUOUS_SELECT, innocuousParameterBuilder, assertNotNullOutputReader);
    transaction.execute();
  }

  @Test
  public void testNullConditionBuilder() throws SQLException {
    doInnocuousSUDI(null, innocuousParameterBuilder, innocuousOutputReader);
  }

  @Test
  public void testNoParameters() throws SQLException {
    doInnocuousSUDI(innocuousCondition, innocuousParameterBuilder, innocuousOutputReader);
    doInnocuousSUDI(innocuousCondition, context -> null, innocuousOutputReader);
  }

  @Test
  public void testNullParametersBuilder() throws SQLException {
    doInnocuousSUDI(innocuousCondition, null, innocuousOutputReader);
  }

  @Test
  public void testNullOutputBuilder() throws SQLException {
    doInnocuousSUDI(innocuousCondition, innocuousParameterBuilder, null);
  }

  @Test
  public void testFalseCondition() throws SQLException {
    doInnocuousSUDI(context -> false, innocuousParameterBuilder, failOutputReader);
  }

  private void doInnocuousSUDI(Function<NormContext<Object, Object>, Boolean> condition, Function<NormContext<Object, Object>, Iterable<Object>> parameterBuilder, NormOutputReader<Object, Object> resultConsumer) throws SQLException {
    NormTransaction<Object, Object> transaction = new NormTransaction<>(connectionSupplier);
    transaction.appendSelect(condition, context -> INNOCUOUS_SELECT, parameterBuilder, resultConsumer);
    transaction.appendUpdate(condition, context -> "update people set name = '42' where false", parameterBuilder, resultConsumer);
    transaction.appendDelete(condition, context -> "delete from people where false", parameterBuilder, resultConsumer);
    transaction.appendInsert(condition, context -> "insert into people(name) (select '42' from people where false)", parameterBuilder, resultConsumer);
    transaction.execute();
  }

  @Test
  public void testCreateInsertUpdateDelete() throws SQLException {
    NormTransaction<String, Integer> t = new NormTransaction<>(connectionSupplier);

    t.appendInsert(context -> "INSERT INTO PEOPLE (NAME) VALUES (?)", context -> Arrays.asList(context.getInput()), NormContext::getFirstGeneratedKeyAsInteger);
    t.appendSelect(context -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?", context -> Arrays.asList(context.getInput()), context -> {
      ResultSet rs = context.getResultSet();
      Assert.assertTrue(rs.next());
      Assert.assertEquals(context.getCurrentOutput(), (Integer) rs.getInt(1));
      return context.getCurrentOutput();
    });
    t.appendUpdate(context -> "UPDATE PEOPLE SET NAME=? WHERE NAME=?", context -> Arrays.asList("Mr. " + context.getInput(), context.getInput()), context -> {
      Assert.assertEquals(1, (int) context.getUpdatedRows());
      return context.getCurrentOutput();
    });

    t.appendSelect(context -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?", context -> Arrays.asList("Mr. " + context.getInput()), context -> {
      ResultSet rs = context.getResultSet();
      rs.next();
      Assert.assertEquals(context.getCurrentOutput(), (Integer) rs.getInt(1));
      return context.getCurrentOutput();
    });

    t.appendDelete(context -> "DELETE FROM PEOPLE WHERE NAME=?", context -> Arrays.asList(context.getInput()), context -> {
      Assert.assertEquals(0, (int) context.getUpdatedRows());
      return context.getCurrentOutput();
    });

    t.appendUpdate(context -> "DELETE FROM PEOPLE WHERE NAME=?", context -> Arrays.asList("Mr. " + context.getInput()), context -> {
      Assert.assertEquals(1, (int) context.getUpdatedRows());
      return context.getCurrentOutput();
    });

    t.appendSelect(context -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?", context -> Arrays.asList("Mr. " + context.getInput()), context -> {
      ResultSet rs = context.getResultSet();
      Assert.assertFalse(rs.next());
      return context.getCurrentOutput();
    });

    t.execute("Obama");
  }

  @Test
  public void testConditionalCreateInsertUpdateDelete() {
    NormTransaction<String, Integer> t = new NormTransaction<>(connectionSupplier);

  }

  @Test
  public void testCreateCallable() {
    // TODO test procedure with IN parameters, OUT parameters, INOUT
    // parameters and an unhealthy mix of them all
  }

  @Test(expected = NormTransactionException.class)
  public void testSelectNoConnectionSupplier() throws SQLException {
    new NormTransaction<>().appendSelect(context -> "values 1", innocuousParameterBuilder, assertNotNullOutputReader).execute();
  }

  @Test(expected = NormTransactionException.class)
  public void testSelectNoConnection() throws SQLException {
    new NormTransaction<>(() -> null).appendSelect(context -> "values 1", innocuousParameterBuilder, assertNotNullOutputReader).execute();
  }

  @Test(expected = NormTransactionException.class)
  public void testSelectNoQueryBuilder() throws SQLException {
    NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
    t.appendSelect(null, innocuousParameterBuilder, assertNotNullOutputReader).execute();
  }

  @Test(expected = NormTransactionException.class)
  public void testSelectNoQuery() throws SQLException {
    NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
    t.appendSelect(context -> null, innocuousParameterBuilder, assertNotNullOutputReader).execute();
  }

  @Test()
  public void testSelect() throws SQLException {
    NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
    t.appendSelect(context -> "values 1", innocuousParameterBuilder, assertNotNullOutputReader).execute();
  }

  @Test(expected = SQLException.class)
  public void testSelectSQLErrorSyntax() throws SQLException {
    NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
    t.appendSelect(context -> "select your_mom", innocuousParameterBuilder, assertNotNullOutputReader).execute();
  }

  @Test(expected = TestSQLException.class)
  public void testSelectSQLErrorOnRead() throws SQLException {
    NormTransaction<Object, Object> t = new NormTransaction<>(connectionSupplier);
    t.appendSelect(context -> "values 1", innocuousParameterBuilder, failOutputReader).execute();
  }

  @Test
  public void testInputAdapter() throws SQLException {
    NormTransaction<PersonBean, Integer> t = new NormTransaction<>(connectionSupplier);

    t.appendInsert(context -> "insert into people(name) values('Obama Bin Laden')", null, null);

    NormSelect<String, Integer> selectByName = new NormSelect<>();
    selectByName.setQueryBuilder(context -> "SELECT PERSON_ID FROM PEOPLE WHERE NAME=?");
    selectByName.setParametersBuilder(context -> Arrays.asList(context.getInput()));
    selectByName.setOutputReader(context -> {
      ResultSet rs = context.getResultSet();
      Assert.assertTrue(rs.next());
      return (Integer) rs.getInt(1);
    });

    PersonBean p = new PersonBean();
    p.setName("Obama Bin Laden");
    t.appendOperation(selectByName, PersonBean::getName, null);

    Assert.assertNotEquals(null, t.execute(p));
  }

  @Test
  public void testGeneratedKeys() throws SQLException {
    Integer[] keyId = new Integer[1];
    NormOutputReader<Void, Integer> genKeysReader = context -> {
      keyId[0] = ((Number) context.getFirstGeneratedKeys().values().iterator().next()).intValue();
      return null;
    };
    NormOutputReader<Void, Integer> genKeyReader = context -> {
      keyId[0] = ((Number) context.getFirstGeneratedKey()).intValue();
      return null;
    };

    NormOutputReader<Void, Integer> genKeyIntegerReader = context -> {
      keyId[0] = context.getFirstGeneratedKeyAsInteger();
      return null;
    };

    for (NormOutputReader<Void, Integer> outputReader : Arrays.asList(genKeysReader, genKeyReader, genKeyIntegerReader)) {
      Integer selectId = buildTestGeneratedKeysTransaction("Strider" + Math.random(), outputReader).execute();
      Assert.assertEquals(selectId, keyId[0]);
    }
  }

  private NormTransaction<Void, Integer> buildTestGeneratedKeysTransaction(String insertName, NormOutputReader<Void, Integer> outputReader) throws SQLException {
    NormTransaction<Void, Integer> transaction = new NormTransaction<>(connectionSupplier);
    transaction.appendInsert(c -> "insert into people(name) values(?)", c -> Arrays.asList(insertName), outputReader);
    transaction.appendSelect(c -> "select person_id, name from people where name = ?",  c -> Arrays.asList(insertName), context -> {
          ResultSet rs = context.getResultSet();
          return rs.next() ? ((Number) rs.getObject("person_id")).intValue() : null;
        });

    return transaction;
  }
}
