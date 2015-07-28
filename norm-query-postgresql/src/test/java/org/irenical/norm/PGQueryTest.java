package org.irenical.norm;

import java.util.Arrays;
import java.util.List;

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
    
    @Test
    public void testSimpleSelect() {
        String expected = "select id,name,age-? as age_back_then from dudes where age>=? limit ? offset ?";
        List<Object> expectedParams = Arrays.asList(4,4,10,12);

        PGSelect qb = PGQueryBuilder.select().literal("id,name,age-").value(4).literal("as age_back_then from dudes where age>=").value(4).literal("limit ").value(10).literal("offset ").value(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age-").value(4).literal("as age_back_then from dudes where age>=").value(4).literal("limit ").value(10).literal("offset ").value(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age-").value(4).literal("as age_back_then from dudes where age>=").value(4).limit().value(10).offset().value(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age-").value(4).literal("as age_back_then from dudes where age>=").value(4).limit(10).offset(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age-").value(4).literal("as age_back_then from dudes where age").gte(4).limit(10).offset(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age").minus(4).literal("as age_back_then from dudes where age").gte(4).limit(10).offset(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age").minus(4).as("age_back_then").literal("from dudes where age").gte(4).limit(10).offset(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age").minus(4).as("age_back_then").from().literal("dudes where age").gte(4).limit(10).offset(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age").minus(4).as("age_back_then").from("dudes").literal("where age").gte(4).limit(10).offset(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age").minus(4).as("age_back_then").from("dudes").where().literal("age").gte(4).limit(10).offset(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id,name,age").minus(4).as("age_back_then").from("dudes").where("age").gte(4).limit(10).offset(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select("id", "name", "age").minus(4).as("age_back_then").from("dudes").where("age").gte(4).limit(10).offset(12);
        Assert.assertEquals(expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
        qb = PGQueryBuilder.select(qb);
        Assert.assertEquals("select " + expected, qb.getQuery());
        Assert.assertEquals(expectedParams, qb.getParameters());
        
    }

}
