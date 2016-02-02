package org.irenical.norm.query.postgresql.builders;

import java.util.Arrays;

import org.irenical.norm.query.NormBaseQueryBuilder;
import org.irenical.norm.query.postgresql.templates.InsertTemplate;

public class InsertBuilder extends NormBaseQueryBuilder<InsertBuilder> implements InsertTemplate {
  
  @Override
  public InsertTemplate into(String table) {
    return literal(" into ").literal(table);
  }

  @Override
  public InsertTemplate columns(Object... columns) {
    return literals(Arrays.asList(columns), "(", ")", ",");
  }

  @Override
  public InsertTemplate defaultValues() {
    return literal(" default values");
  }

  @Override
  public InsertTemplate values(Object... values) {
    return values(Arrays.asList(values), " values(", ")", ",");
  }

  @Override
  public InsertTemplate query(Object query) {
    return literal(query);
  }

  @Override
  public InsertTemplate returningAll() {
    return literal(" returning *");
  }

  @Override
  public InsertTemplate returning(Object... expressions) {
    return literals(Arrays.asList(expressions), " ", null, ",");
  }

}
