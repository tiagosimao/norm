package org.irenical.norm.query;

import java.util.LinkedList;
import java.util.List;

public class NormQueryBuilder {
    
    private static final char VALUE = '?';

    private final List<Object> parameters = new LinkedList<Object>();

    private final StringBuilder sb = new StringBuilder();

    private NormQueryBuilder(){
    }
    
    public static NormQueryBuilder create(){
        return new NormQueryBuilder();
    }
    
    public List<Object> getParameters() {
        return parameters;
    }

    public String getQuery() {
        return sb.toString();
    }
    
    public NormQueryBuilder literal(String sql){
        sb.append(sql);
        return this;
    }
    
    public NormQueryBuilder value(Object value){
        sb.append(VALUE);
        parameters.add(value);
        return this;
    }
    
}
