package org.irenical.norm.transaction;

import java.util.function.Function;

public class NormOperationAdapter<INPUT, OUTPUT, OP_INPUT, OP_OUTPUT> {

  private final NormOperation<OP_INPUT, OP_OUTPUT> operation;

  private Function<INPUT, OP_INPUT> inputAdapter;

  private Function<OUTPUT, OP_OUTPUT> outputAdapter;
  
  protected NormOperationAdapter(NormOperation<OP_INPUT, OP_OUTPUT> operation){
    this.operation=operation;
  }

  public void setInputAdapter(Function<INPUT, OP_INPUT> inputAdapter) {
    this.inputAdapter = inputAdapter;
  }

  public void setOutputAdapter(Function<OUTPUT, OP_OUTPUT> outputAdapter) {
    this.outputAdapter = outputAdapter;
  }

  public Function<INPUT, OP_INPUT> getInputAdapter() {
    return inputAdapter;
  }

  public NormOperation<OP_INPUT, OP_OUTPUT> getOperation() {
    return operation;
  }

  public Function<OUTPUT, OP_OUTPUT> getOutputAdapter() {
    return outputAdapter;
  }

}
