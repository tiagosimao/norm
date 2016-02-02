
package org.irenical.norm.query.postgresql.templates;

import org.irenical.norm.query.NormQueryBuilder;
import org.irenical.norm.query.postgresql.builders.InsertBuilder;

public interface InsertTemplate extends NormQueryBuilder<InsertBuilder> {

  InsertTemplate into(String table);

  InsertTemplate columns(Object... columns);

  InsertTemplate defaultValues();

  InsertTemplate values(Object... values);

  InsertTemplate query(Object query);

  InsertTemplate returningAll();

  InsertTemplate returning(Object... expressions);

}
