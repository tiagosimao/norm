package org.irenical.norm.query.postgresql;

public interface PGExpression {
    
    PGSelect not();
    
    PGSelect notEq(Object... that);

    PGSelect eq(Object... that);
    
    PGSelect gt(Object... that);

    PGSelect gte(Object... that);

    PGSelect divide(Object... that);

    PGSelect multiply(Object... that);

    PGSelect minus(Object... that);

    PGSelect plus(Object... that);

    PGSelect lt(Object... than);

    PGSelect lte(Object... that);

    PGSelect in(Object... that);

    PGSelect like(Object... that);

    PGSelect ilike(Object... that);

}
