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
    public PGSelect as() {
        return literal("as");
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
    public PGExpression eq() {
        return super.literal("=");
    }

    @Override
    public PGSelect except() {
        return literal("except");
    }

    @Override
    public PGSelect from(String... from) {
        return literal("from").literals(Arrays.asList(from), " ", null, ",");

    }

    @Override
    public PGSelect groupby() {
        return literal("group by");
    }

    @Override
    public PGExpression gt() {
        return super.literal(">");
    }

    @Override
    public PGExpression lte() {
        return super.literal("<=");
    }

    @Override
    public PGExpression lt() {
        return super.literal("<");
    }

    @Override
    public PGExpression gte() {
        return super.literal(">=");
    }

    @Override
    public PGSelect having() {
        return literal("having");
    }

    @Override
    public PGExpression ilike() {
        return literal("ilike");
    }

    @Override
    public PGExpression in() {
        return literal("in");
    }

    @Override
    public PGExpression like() {
        return literal("like");
    }

    @Override
    public PGSelect intersect() {
        return literal("intersect");
    }

    @Override
    public PGSelect limit() {
        return literal("limit");
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
    public PGSelect offset() {
        return literal("offset");
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
    public PGSelect where() {
        return literal("where");
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
    public PGSelect literal(String sql) {
        super.literal(" ");
        return super.literal(sql);
    }

}
