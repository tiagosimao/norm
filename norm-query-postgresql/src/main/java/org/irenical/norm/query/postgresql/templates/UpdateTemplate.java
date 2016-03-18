
package org.irenical.norm.query.postgresql.templates;

import org.irenical.norm.query.NormQueryBuilder;

public interface UpdateTemplate extends NormQueryBuilder<UpdateTemplate> {
  
  public UpdateTemplate only();
  
  public UpdateTemplate table(String table);
  
  public UpdateTemplate all();
  
  public UpdateTemplate as(String alias);
  
  public UpdateTemplate set();
  
  public UpdateTemplate columnDefault(String column);
  
  public UpdateTemplate column(String column, Object expression);
  
  public UpdateTemplate from();
  
}
