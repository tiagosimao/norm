package org.irenical.norm.query.postgresql.builders;

import java.util.Arrays;

import org.irenical.norm.query.NormBaseQueryBuilder;
import org.irenical.norm.query.postgresql.PGExpression;

public class PGExpressionBuilder extends NormBaseQueryBuilder<PGExpression> implements PGExpression {

    public PGExpressionBuilder() {
    }

    @Override
    public PGExpression not() {
        return literal("not");
    }

    @Override
    public PGExpression in(Object... these) {
        if (these == null || these.length == 0) {
            return literal("in");
        } else {
            return eq(these);
        }
    }

    @Override
    public PGExpression eq(Object... that) {
        if (that != null) {
            if (that.length == 1) {
                if (that[0] == null) {
                    return literal("is null");
                } else {
                    return super.literal("=").value(that[0]);
                }
            } else {
                return values(Arrays.asList(that), " in(", ")", ",");
            }
        } else {
            return super.literal("=");
        }
    }

    @Override
    public PGExpression notEq(Object... that) {
        if (that != null) {
            if (that.length == 1) {
                if (that[0] == null) {
                    return literal("is not null");
                } else {
                    return super.literal("!=").value(that[0]);
                }
            } else {
                literal("not");
                return in(that);
            }
        } else {
            return super.literal("!=");
        }
    }

    private PGExpression binaryOperation(String op, Object... that) {
        if (that == null || that.length == 0) {
            return super.literal(op);
        } else {
            return super.literal(op).value(that[0]);
        }
    }

    @Override
    public PGExpression gt(Object... that) {
        return binaryOperation(">", that);
    }

    @Override
    public PGExpression lte(Object... that) {
        return binaryOperation("<=", that);
    }

    @Override
    public PGExpression lt(Object... that) {
        return binaryOperation("<", that);
    }

    @Override
    public PGExpression gte(Object... that) {
        return binaryOperation(">=", that);
    }

    @Override
    public PGExpression ilike(Object... that) {
        return binaryOperation("ilike", that);
    }

    @Override
    public PGExpression like(Object... that) {
        return binaryOperation("like", that);
    }

    @Override
    public PGExpression divide(Object... that) {
        return binaryOperation("/", that);
    }

    @Override
    public PGExpression multiply(Object... that) {
        return binaryOperation("*", that);
    }

    @Override
    public PGExpression minus(Object... that) {
        return binaryOperation("-", that);
    }

    @Override
    public PGExpression plus(Object... that) {
        return binaryOperation("+", that);
    }
    
    @Override
    public PGExpression or(Object... that) {
        return null;
    }
    
    @Override
    public PGExpression and(Object... that) {
        return null;
    }

    @Override
    public PGExpression literal(Object sql) {
        super.literal(" ");
        return super.literal(sql);
    }
    
    @Override
    public String toString() {
        return getQuery();
    }

}
