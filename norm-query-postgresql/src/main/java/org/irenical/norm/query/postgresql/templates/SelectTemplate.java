package org.irenical.norm.query.postgresql.templates;


public interface SelectTemplate extends ExpressionTemplate<SelectTemplate> {

    SelectTemplate with();

    SelectTemplate recursive();

    SelectTemplate all();

    SelectTemplate distinct();

    SelectTemplate distinctOn();

    SelectTemplate asterisk();

    SelectTemplate from(Object... entities);

    SelectTemplate where(Object... that);

    SelectTemplate groupby();

    SelectTemplate having();

    SelectTemplate as(Object... alias);

    SelectTemplate union();

    SelectTemplate intersect();

    SelectTemplate except();

    SelectTemplate select();

    SelectTemplate orderby();

    SelectTemplate asc();

    SelectTemplate desc();

    SelectTemplate using();

    SelectTemplate nullsFirst();

    SelectTemplate nullsLast();

    SelectTemplate limit(int... limit);

    SelectTemplate offset(int... offset);

    SelectTemplate join();

    SelectTemplate naturalInnerJoin();

    SelectTemplate innerJoin();

    SelectTemplate crossJoin();

    SelectTemplate leftJoin();

    SelectTemplate rightJoin();

    SelectTemplate fullJoin();

    SelectTemplate naturalLeftJoin();

    SelectTemplate naturalRightJoin();

    SelectTemplate naturalFullJoin();

}
