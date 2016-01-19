package org.irenical.norm.query.postgresql;

import java.util.Arrays;

import org.irenical.norm.query.postgresql.builders.PGExpressionBuilder;
import org.irenical.norm.query.postgresql.builders.PGSelectBuilder;
import org.irenical.norm.query.postgresql.templates.SelectTemplate;

public class PGQuery {

    private PGQuery() {
    }

    public static SelectTemplate select(Object... commaSeparatedLiterals) {
        PGSelectBuilder qb = new PGSelectBuilder("select");
        if (commaSeparatedLiterals != null && commaSeparatedLiterals.length > 0) {
            qb.literals(Arrays.asList(commaSeparatedLiterals), " ", null, ",");
        }
        return qb;
    }

    public static PGExpression expression(String prefix) {
        PGExpressionBuilder qb = new PGExpressionBuilder();
        if (prefix != null) {
            qb.literal(prefix);
        }
        return qb;
    }

}
