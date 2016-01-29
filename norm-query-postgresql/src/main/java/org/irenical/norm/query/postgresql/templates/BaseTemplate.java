package org.irenical.norm.query.postgresql.templates;

import org.irenical.norm.query.NormQueryBuilder;

public interface BaseTemplate<BUILDER_CLASS extends NormQueryBuilder<BUILDER_CLASS>> extends NormQueryBuilder<BUILDER_CLASS> {
    
    BUILDER_CLASS not();
    
    BUILDER_CLASS notEq(Object... that);

    BUILDER_CLASS eq(Object... that);
    
    BUILDER_CLASS gt(Object... that);

    BUILDER_CLASS gte(Object... that);

    BUILDER_CLASS divide(Object... that);

    BUILDER_CLASS multiply(Object... that);

    BUILDER_CLASS minus(Object... that);

    BUILDER_CLASS plus(Object... that);

    BUILDER_CLASS lt(Object... than);

    BUILDER_CLASS lte(Object... that);

    BUILDER_CLASS in(Object... that);

    BUILDER_CLASS like(Object... that);

    BUILDER_CLASS ilike(Object... that);
    
    BUILDER_CLASS or(Object... that);
    
    BUILDER_CLASS and(Object... that);
    
}
