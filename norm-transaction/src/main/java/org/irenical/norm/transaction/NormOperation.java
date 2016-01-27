package org.irenical.norm.transaction;

import java.sql.SQLException;
import java.util.function.Function;

public abstract class NormOperation<INPUT, OUTPUT> {

  protected Function<NormContext<INPUT, OUTPUT>, String> queryBuilder;

  protected Function<NormContext<INPUT, OUTPUT>, Iterable<Object>> parametersBuilder;

  protected NormOutputReader<INPUT, OUTPUT> outputReader;

  protected Function<NormContext<INPUT, OUTPUT>, Boolean> condition;

  abstract OUTPUT execute(NormContext<INPUT, OUTPUT> context) throws SQLException;

  public void setParametersBuilder(Function<NormContext<INPUT, OUTPUT>, Iterable<Object>> parametersBuilder) {
    this.parametersBuilder = parametersBuilder;
  }

  public void setQueryBuilder(Function<NormContext<INPUT, OUTPUT>, String> queryBuilder) {
    this.queryBuilder = queryBuilder;
  }

  public Function<NormContext<INPUT, OUTPUT>, Iterable<Object>> getParametersBuilder() {
    return parametersBuilder;
  }

  public Function<NormContext<INPUT, OUTPUT>, String> getQueryBuilder() {
    return queryBuilder;
  }

  public void setOutputReader(NormOutputReader<INPUT, OUTPUT> outputReader) {
    this.outputReader = outputReader;
  }

  public NormOutputReader<INPUT, OUTPUT> getOutputReader() {
    return outputReader;
  }

  public void setCondition(Function<NormContext<INPUT, OUTPUT>, Boolean> condition) {
    this.condition = condition;
  }

  public Function<NormContext<INPUT, OUTPUT>, Boolean> getCondition() {
    return condition;
  }

}
