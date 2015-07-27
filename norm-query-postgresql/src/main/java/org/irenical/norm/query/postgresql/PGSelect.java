package org.irenical.norm.query.postgresql;

import org.irenical.norm.query.NormQueryBuilder;


public interface PGSelect extends PGExpression, NormQueryBuilder<PGSelect> {

    PGSelect with();

    PGSelect recursive();

    PGSelect all();

    PGSelect distinct();

    PGSelect distinctOn();

    PGSelect asterisk();

    PGSelect from(String ... entities);

    PGSelect where();

    PGSelect groupby();

    PGSelect having();

    PGSelect as();

    PGSelect union();

    PGSelect intersect();

    PGSelect except();

    PGSelect select();

    PGSelect orderby();

    PGSelect asc();

    PGSelect desc();

    PGSelect using();

    PGSelect nullsFirst();

    PGSelect nullsLast();

    PGSelect limit();

    PGSelect offset();

    PGSelect join();

    PGSelect naturalInnerJoin();

    PGSelect innerJoin();

    PGSelect crossJoin();

    PGSelect leftJoin();

    PGSelect rightJoin();

    PGSelect fullJoin();

    PGSelect naturalLeftJoin();

    PGSelect naturalRightJoin();

    PGSelect naturalFullJoin();
    
}
