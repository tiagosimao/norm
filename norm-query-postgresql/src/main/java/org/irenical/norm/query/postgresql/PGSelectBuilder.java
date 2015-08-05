package org.irenical.norm.query.postgresql;

import java.util.Arrays;

import org.irenical.norm.query.NormBaseQueryBuilder;

public class PGSelectBuilder extends NormBaseQueryBuilder<PGSelect> implements PGSelect {

    protected PGSelectBuilder(String prefix) {
        super.literal(prefix);
    }

    @Override
    public PGSelect all() {
        return literal("all");
    }

    @Override
    public PGSelect as(Object... alias) {
        if (alias == null || alias.length == 0) {
            return literal("as");
        } else {
            return literal("as").literal(alias[0]);
        }
    }

    @Override
    public PGSelect asc() {
        return literal("asc");
    }

    @Override
    public PGSelect desc() {
        return literal("desc");
    }

    @Override
    public PGSelect asterisk() {
        return literal("*");
    }

    @Override
    public PGSelect distinct() {
        return literal("distinct");
    }

    @Override
    public PGSelect distinctOn() {
        return literal("distinct on");
    }

    @Override
    public PGSelect crossJoin() {
        return literal("cross join");
    }

    @Override
    public PGSelect innerJoin() {
        return literal("inner join");
    }

    @Override
    public PGSelect fullJoin() {
        return literal("full outer join");
    }

    @Override
    public PGSelect leftJoin() {
        return literal("left outer join");
    }

    @Override
    public PGSelect rightJoin() {
        return literal("right outer join");
    }

    @Override
    public PGSelect naturalFullJoin() {
        return literal("natural full outer join");
    }

    @Override
    public PGSelect naturalInnerJoin() {
        return literal("natural inner join");
    }

    @Override
    public PGSelect naturalLeftJoin() {
        return literal("natural left outer join");
    }

    @Override
    public PGSelect naturalRightJoin() {
        return literal("natural right outer join");
    }

    @Override
    public PGSelect join() {
        return literal("natural join");
    }

    @Override
    public PGSelect not() {
        return literal("not");
    }

    @Override
    public PGSelect except() {
        return literal("except");
    }

    @Override
    public PGSelect from(Object... from) {
        return literal("from").literals(Arrays.asList(from), " ", null, ",");

    }

    @Override
    public PGSelect groupby() {
        return literal("group by");
    }

    @Override
    public PGSelect having() {
        return literal("having");
    }

    @Override
    public PGSelect in(Object... these) {
        if (these == null || these.length == 0) {
            return literal("in");
        } else {
            return eq(these);
        }
    }

    @Override
    public PGSelect eq(Object... that) {
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
    public PGSelect notEq(Object... that) {
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

    private PGSelect binaryOperation(String op, Object... that) {
        if (that == null || that.length == 0) {
            return super.literal(op);
        } else {
            return super.literal(op).value(that[0]);
        }
    }

    @Override
    public PGSelect gt(Object... that) {
        return binaryOperation(">", that);
    }

    @Override
    public PGSelect lte(Object... that) {
        return binaryOperation("<=", that);
    }

    @Override
    public PGSelect lt(Object... that) {
        return binaryOperation("<", that);
    }

    @Override
    public PGSelect gte(Object... that) {
        return binaryOperation(">=", that);
    }

    @Override
    public PGSelect ilike(Object... that) {
        return binaryOperation("ilike", that);
    }

    @Override
    public PGSelect like(Object... that) {
        return binaryOperation("like", that);
    }

    @Override
    public PGSelect divide(Object... that) {
        return binaryOperation("/", that);
    }

    @Override
    public PGSelect multiply(Object... that) {
        return binaryOperation("*", that);
    }

    @Override
    public PGSelect minus(Object... that) {
        return binaryOperation("-", that);
    }

    @Override
    public PGSelect plus(Object... that) {
        return binaryOperation("+", that);
    }

    @Override
    public PGSelect intersect() {
        return literal("intersect");
    }

    @Override
    public PGSelect limit(int... limit) {
        if (limit != null && limit.length > 0) {
            return literal("limit ").value(limit[0]);
        }
        return literal("limit ");
    }

    @Override
    public PGSelect offset(int... offset) {
        if (offset != null && offset.length > 0) {
            return literal("offset ").value(offset[0]);
        }
        return literal("offset ");
    }

    @Override
    public PGSelect nullsFirst() {
        return literal("nulls first");
    }

    @Override
    public PGSelect nullsLast() {
        return literal("nulls last");
    }

    @Override
    public PGSelect orderby() {
        return literal("order by");
    }

    @Override
    public PGSelect recursive() {
        return literal("recursive");
    }

    @Override
    public PGSelect union() {
        return literal("union");
    }

    @Override
    public PGSelect using() {
        return literal("using");
    }

    @Override
    public PGSelect where(Object... that) {
        if (that == null || that.length == 0) {
            return literal("where");
        } else {
            return literal("where").literal(that[0]);
        }

    }

    @Override
    public PGSelect select() {
        return null;
    }

    @Override
    public PGSelect with() {
        return null;
    }

    @Override
    public PGSelect literal(Object sql) {
        super.literal(" ");
        return super.literal(sql);
    }

}
