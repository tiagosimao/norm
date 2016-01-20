package org.irenical.norm.transaction;

import java.sql.SQLException;
import java.util.function.Function;

public abstract class NTOperation<INPUT, OUTPUT> {

    protected Function<NTContext<INPUT, OUTPUT>, String> queryBuilder;

    protected Function<NTContext<INPUT, OUTPUT>, Iterable<Object>> parametersBuilder;

    protected NTOutputReader<INPUT, OUTPUT> outputReader;

    abstract OUTPUT execute(NTContext<INPUT, OUTPUT> context) throws SQLException;

    public void setParametersBuilder(Function<NTContext<INPUT, OUTPUT>, Iterable<Object>> parametersBuilder) {
        this.parametersBuilder = parametersBuilder;
    }

    public void setQueryBuilder(Function<NTContext<INPUT, OUTPUT>, String> queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public Function<NTContext<INPUT, OUTPUT>, Iterable<Object>> getParametersBuilder() {
        return parametersBuilder;
    }

    public Function<NTContext<INPUT, OUTPUT>, String> getQueryBuilder() {
        return queryBuilder;
    }

    public void setResultConsumer(NTOutputReader<INPUT, OUTPUT> resultConsumer) {
        this.outputReader = resultConsumer;
    }

    public NTOutputReader<INPUT, OUTPUT> getResultConsumer() {
        return outputReader;
    }

}
