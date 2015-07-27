package org.irenical.norm;

import org.irenical.norm.query.postgresql.PGQueryBuilder;
import org.irenical.norm.query.postgresql.PGSelect;
import org.junit.Assert;
import org.junit.Test;

public class PGQueryTest {

    @Test
    public void testTrivialSelect() {
        String expected = "select * from some_table";

        PGSelect qb = PGQueryBuilder.select("* from some_table");
        Assert.assertEquals(expected, qb.getQuery());

        qb = PGQueryBuilder.select("*").from("some_table");
        Assert.assertEquals(expected, qb.getQuery());

        qb = PGQueryBuilder.select().asterisk().from("some_table");
        Assert.assertEquals(expected, qb.getQuery());

        qb = PGQueryBuilder.select().asterisk().from().literal("some_table");
        Assert.assertEquals(expected, qb.getQuery());

    }

}
