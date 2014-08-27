package org.irenical.norm.sql;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tgsimao on 08/05/14.
 */
public class Condition extends Expression {

    protected final List<Expression> operands = new LinkedList<>();

    protected Condition(){
    }

    public Column c(String name) {
        Column operand = new Column();
        operand.expression=name;
        operands.add(operand);
        return this;
    }

    public Expression x(String expression){
        Expression result = new Column();
        result.parent=this;
        result.expression=expression;
        columns.add(result);
        return result;
    }

}
