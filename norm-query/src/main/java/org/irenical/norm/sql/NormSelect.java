package org.irenical.norm.sql;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tgsimao on 08/05/14.
 */
public class NormSelect extends NormQuery {

    protected boolean distinct;

    protected boolean all;

    protected String alias;

    protected final List<Expression> columns = new LinkedList<>();

    protected Condition where;
    
    public NormSelect(String ... columns){
    	if(columns!=null){
    		for(String column : columns){
    			c(column);
    		}
    	}
    }

    public Column c(String name) {
        Column result = new Column();
        result.parent=this;
        result.expression=name;
        columns.add(result);
        return result;
    }

    public Expression x(String expression){
        Expression result = new Column();
        result.parent=this;
        result.expression=expression;
        columns.add(result);
        return result;
    }

    public NormSelect as(String alias){
        this.alias=alias;
        return this;
    }

    public Condition where() {
        where = new Condition();
        where.parent=this;
        return where;
    }

}


