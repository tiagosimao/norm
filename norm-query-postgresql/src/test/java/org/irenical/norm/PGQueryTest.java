package org.irenical.norm;

import java.util.Arrays;
import java.util.List;

import org.irenical.norm.query.postgresql.PostgresQueryBuilder;
import org.irenical.norm.query.postgresql.templates.ExpressionTemplate;
import org.irenical.norm.query.postgresql.templates.SelectTemplate;
import org.junit.Assert;
import org.junit.Test;

public class PGQueryTest {
  
  private final PostgresQueryBuilder builder = new PostgresQueryBuilder();

  @Test
  public void testTrivialSelect() {
    String expected = "select * from some_table";

    SelectTemplate qb = builder.createSelect("* from some_table");
    Assert.assertEquals(expected, qb.getQuery());

    qb = builder.createSelect("*").from("some_table");
    Assert.assertEquals(expected, qb.getQuery());

    qb = builder.createSelect().asterisk().from("some_table");
    Assert.assertEquals(expected, qb.getQuery());

    qb = builder.createSelect().asterisk().from().literal("some_table");
    Assert.assertEquals(expected, qb.getQuery());

  }

  @Test
  public void testSimpleSelect() {
    String expected = "select id,name,age-? as age_back_then from dudes where age>=? limit ? offset ?";
    List<Object> expectedParams = Arrays.asList(4, 4, 10, 12);

    SelectTemplate qb = builder.createSelect().literal("id,name,age-").value(4).literal("as age_back_then from dudes where age>=").value(4).literal("limit ").value(10).literal("offset ").value(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age-").value(4).literal("as age_back_then from dudes where age>=").value(4).literal("limit ").value(10).literal("offset ").value(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age-").value(4).literal("as age_back_then from dudes where age>=").value(4).limit().value(10).offset().value(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age-").value(4).literal("as age_back_then from dudes where age>=").value(4).limit(10).offset(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age-").value(4).literal("as age_back_then from dudes where age").gte(4).limit(10).offset(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age").minus(4).literal("as age_back_then from dudes where age").gte(4).limit(10).offset(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age").minus(4).as("age_back_then").literal("from dudes where age").gte(4).limit(10).offset(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age").minus(4).as("age_back_then").from().literal("dudes where age").gte(4).limit(10).offset(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age").minus(4).as("age_back_then").from("dudes").literal("where age").gte(4).limit(10).offset(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age").minus(4).as("age_back_then").from("dudes").where().literal("age").gte(4).limit(10).offset(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id,name,age").minus(4).as("age_back_then").from("dudes").where("age").gte(4).limit(10).offset(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect("id", "name", "age").minus(4).as("age_back_then").from("dudes").where("age").gte(4).limit(10).offset(12);
    Assert.assertEquals(expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

    qb = builder.createSelect(qb);
    Assert.assertEquals("select " + expected, qb.getQuery());
    Assert.assertEquals(expectedParams, qb.getParameters());

  }

  @Test
  public void testExpression() {

    String expectedCondition = "age>? and name ilike ? or surname ilike ?";
    List<Object> expectedConditionParams = Arrays.asList(20, "%joe%", "%joe%");

    String expected = "select count(id) from dudes where " + expectedCondition;
    List<Object> expectedParams = expectedConditionParams;

    ExpressionTemplate condition = builder.createExpression("age").gt(20).and(builder.createExpression("name").ilike("%joe%").or("surname").ilike("%joe%"));
    Assert.assertEquals(expectedCondition, condition.getQuery());
    Assert.assertEquals(expectedConditionParams, condition.getParameters());

    SelectTemplate select = builder.createSelect("count(id)").from("dudes").where(condition);

    Assert.assertEquals(expected, select.getQuery());
    Assert.assertEquals(expectedParams, select.getParameters());
  }

}
