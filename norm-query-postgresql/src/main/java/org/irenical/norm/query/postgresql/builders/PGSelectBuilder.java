package org.irenical.norm.query.postgresql.builders;

import java.util.Arrays;

import org.irenical.norm.query.NormBaseQueryBuilder;
import org.irenical.norm.query.postgresql.templates.SelectTemplate;

public class PGSelectBuilder extends NormBaseQueryBuilder<SelectTemplate> implements SelectTemplate {

    public PGSelectBuilder(String prefix) {
        super.literal(prefix);
    }
    
    @Override
    public SelectTemplate or(Object... that) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public SelectTemplate and(Object... that) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SelectTemplate all() {
        return literal("all");
    }

    @Override
    public SelectTemplate as(Object... alias) {
        if (alias == null || alias.length == 0) {
            return literal("as");
        } else {
            return literal("as").literal(alias[0]);
        }
    }

    @Override
    public SelectTemplate asc() {
        return literal("asc");
    }

    @Override
    public SelectTemplate desc() {
        return literal("desc");
    }

    @Override
    public SelectTemplate asterisk() {
        return literal("*");
    }

    @Override
    public SelectTemplate distinct() {
        return literal("distinct");
    }

    @Override
    public SelectTemplate distinctOn() {
        return literal("distinct on");
    }

    @Override
    public SelectTemplate crossJoin() {
        return literal("cross join");
    }

    @Override
    public SelectTemplate innerJoin() {
        return literal("inner join");
    }

    @Override
    public SelectTemplate fullJoin() {
        return literal("full outer join");
    }

    @Override
    public SelectTemplate leftJoin() {
        return literal("left outer join");
    }

    @Override
    public SelectTemplate rightJoin() {
        return literal("right outer join");
    }

    @Override
    public SelectTemplate naturalFullJoin() {
        return literal("natural full outer join");
    }

    @Override
    public SelectTemplate naturalInnerJoin() {
        return literal("natural inner join");
    }

    @Override
    public SelectTemplate naturalLeftJoin() {
        return literal("natural left outer join");
    }

    @Override
    public SelectTemplate naturalRightJoin() {
        return literal("natural right outer join");
    }

    @Override
    public SelectTemplate join() {
        return literal("natural join");
    }

    @Override
    public SelectTemplate not() {
        return literal("not");
    }

    @Override
    public SelectTemplate except() {
        return literal("except");
    }

    @Override
    public SelectTemplate from(Object... from) {
        return literal("from").literals(Arrays.asList(from), " ", null, ",");

    }

    @Override
    public SelectTemplate groupby() {
        return literal("group by");
    }

    @Override
    public SelectTemplate having() {
        return literal("having");
    }

    @Override
    public SelectTemplate in(Object... these) {
        if (these == null || these.length == 0) {
            return literal("in");
        } else {
            return eq(these);
        }
    }

    @Override
    public SelectTemplate eq(Object... that) {
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
    public SelectTemplate notEq(Object... that) {
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

    private SelectTemplate binaryOperation(String op, Object... that) {
        if (that == null || that.length == 0) {
            return super.literal(op);
        } else {
            return super.literal(op).value(that[0]);
        }
    }

    @Override
    public SelectTemplate gt(Object... that) {
        return binaryOperation(">", that);
    }

    @Override
    public SelectTemplate lte(Object... that) {
        return binaryOperation("<=", that);
    }

    @Override
    public SelectTemplate lt(Object... that) {
        return binaryOperation("<", that);
    }

    @Override
    public SelectTemplate gte(Object... that) {
        return binaryOperation(">=", that);
    }

    @Override
    public SelectTemplate ilike(Object... that) {
        return binaryOperation("ilike", that);
    }

    @Override
    public SelectTemplate like(Object... that) {
        return binaryOperation("like", that);
    }

    @Override
    public SelectTemplate divide(Object... that) {
        return binaryOperation("/", that);
    }

    @Override
    public SelectTemplate multiply(Object... that) {
        return binaryOperation("*", that);
    }

    @Override
    public SelectTemplate minus(Object... that) {
        return binaryOperation("-", that);
    }

    @Override
    public SelectTemplate plus(Object... that) {
        return binaryOperation("+", that);
    }

    @Override
    public SelectTemplate intersect() {
        return literal("intersect");
    }

    @Override
    public SelectTemplate limit(int... limit) {
        if (limit != null && limit.length > 0) {
            return literal("limit ").value(limit[0]);
        }
        return literal("limit ");
    }

    @Override
    public SelectTemplate offset(int... offset) {
        if (offset != null && offset.length > 0) {
            return literal("offset ").value(offset[0]);
        }
        return literal("offset ");
    }

    @Override
    public SelectTemplate nullsFirst() {
        return literal("nulls first");
    }

    @Override
    public SelectTemplate nullsLast() {
        return literal("nulls last");
    }

    @Override
    public SelectTemplate orderby() {
        return literal("order by");
    }

    @Override
    public SelectTemplate recursive() {
        return literal("recursive");
    }

    @Override
    public SelectTemplate union() {
        return literal("union");
    }

    @Override
    public SelectTemplate using() {
        return literal("using");
    }

    @Override
    public SelectTemplate where(Object... that) {
        if (that == null || that.length == 0) {
            return literal("where");
        } else {
            return literal("where").literal(that[0]);
        }

    }

    @Override
    public SelectTemplate select() {
        return null;
    }

    @Override
    public SelectTemplate with() {
        return null;
    }

    @Override
    public SelectTemplate literal(Object sql) {
        super.literal(" ");
        return super.literal(sql);
    }
    
    @Override
    public String toString() {
        return getQuery();
    }

}
