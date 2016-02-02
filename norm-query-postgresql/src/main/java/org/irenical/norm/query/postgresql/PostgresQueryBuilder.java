package org.irenical.norm.query.postgresql;

import java.util.Arrays;

import org.irenical.norm.query.SimpleQueryBuilder;
import org.irenical.norm.query.postgresql.builders.ExpressionBuilder;
import org.irenical.norm.query.postgresql.builders.InsertBuilder;
import org.irenical.norm.query.postgresql.builders.SelectBuilder;
import org.irenical.norm.query.postgresql.templates.ExpressionTemplate;
import org.irenical.norm.query.postgresql.templates.InsertTemplate;
import org.irenical.norm.query.postgresql.templates.SelectTemplate;

public class PostgresQueryBuilder {
  
  /**
   * Creates a new empty general purpose query builder
   * @return Returns a new instance of SimpleQueryBuilder
   */
  public SimpleQueryBuilder createQuery() {
    return new SimpleQueryBuilder();
  }

  /**
   * Creates a new SELECT operation
   * @param commaSeparatedLiterals - zero or more literals, a convenient way to
   * declare the SELECT's fields. Ex: createSelect("id", "name") will create an
   * expression starting with SELECT id, name...
   * @return Returns a new instance of a SelectTemplate
   */
  public SelectTemplate createSelect(Object... commaSeparatedLiterals) {
    SelectBuilder qb = new SelectBuilder("select");
    if (commaSeparatedLiterals != null && commaSeparatedLiterals.length > 0) {
      qb.literals(Arrays.asList(commaSeparatedLiterals), " ", null, ",");
    }
    return qb;
  }

  public ExpressionTemplate createExpression(String prefix) {
    ExpressionBuilder qb = new ExpressionBuilder();
    if (prefix != null) {
      qb.literal(prefix);
    }
    return qb;
  }
  
  public InsertTemplate createInsert(String table) {
    return new InsertBuilder().literal("insert into ").literal(table);
  }

  public InsertTemplate createInsert() {
    return new InsertBuilder().literal("insert");
  }

}
