package org.irenical.norm.query;

import java.util.List;

public interface NormQueryBuilder<BUILDER_CLASS extends NormQueryBuilder<BUILDER_CLASS>> {

    public abstract List<Object> getParameters();

    public abstract String getQuery();

    /**
     * Append literal value
     * @param sql - the SQL query fragment to be appended
     * @return the builder
     */
    public abstract BUILDER_CLASS literal(Object sql);
    
    
    public abstract BUILDER_CLASS literals(Iterable<Object> sql, String prefix, String suffix, String separator);
    
    /**
     * Append builder
     * @param sql
     * @return
     */
    public abstract BUILDER_CLASS builder(NormQueryBuilder<?>... builders);
    
    /**
     * Append a value. A ? will be appended to the query 
     * @param value - the object representing the value
     * @return the builder
     */
    public abstract BUILDER_CLASS value(Object value);
    
    /**
     * Append multiple values. A ? will be appended to the query for each value
     * Useful for IN expressions
     * @param values - the objects representing the values
     * @param prefix - a literal prepended to the values
     * @param suffix - a literal postpended to the values
     * @param separator - a literal separating each value
     * @return the builder
     */
    public abstract BUILDER_CLASS values(Iterable<Object> values, String prefix, String suffix, String separator);

}