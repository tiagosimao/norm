package org.irenical.norm.query;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class QueryTest {

    @Test
    public void testLiteralQuery() {
        SimpleQueryBuilder qb = new SimpleQueryBuilder();
        qb.literal("select * from some_table");
        Assert.assertTrue("select * from some_table".equals(qb.getQuery()));
        Assert.assertTrue(qb.getParameters() == null || qb.getParameters().isEmpty());
    }

    @Test
    public void testSingleParameterQuery() {
        SimpleQueryBuilder qb = new SimpleQueryBuilder();
        qb.literal("select * from some_table where some_column=").value(3);
        Assert.assertTrue("select * from some_table where some_column=?".equals(qb.getQuery()));
        Assert.assertTrue(qb.getParameters().size() == 1);
        Assert.assertTrue(qb.getParameters().get(0) instanceof Integer);
        Assert.assertTrue(((Integer) qb.getParameters().get(0)).equals(3));
    }

    @Test
    public void testMultipleParameterQuery() {
        SimpleQueryBuilder qb = new SimpleQueryBuilder();
        qb.literal("select * from some_table where some_column in").values(Arrays.asList(3, 5, 7), "(", ")", ",");
        Assert.assertTrue("select * from some_table where some_column in(?,?,?)".equals(qb.getQuery()));
        Assert.assertTrue(qb.getParameters().size() == 3);
        Assert.assertTrue(qb.getParameters().get(0) instanceof Integer);
        Assert.assertTrue(qb.getParameters().get(1) instanceof Integer);
        Assert.assertTrue(qb.getParameters().get(2) instanceof Integer);
        Assert.assertTrue(((Integer) qb.getParameters().get(0)).equals(3));
        Assert.assertTrue(((Integer) qb.getParameters().get(1)).equals(5));
        Assert.assertTrue(((Integer) qb.getParameters().get(2)).equals(7));
    }

}
