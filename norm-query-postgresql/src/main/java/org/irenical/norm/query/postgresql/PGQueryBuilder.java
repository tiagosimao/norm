package org.irenical.norm.query.postgresql;

import java.util.Arrays;

public class PGQueryBuilder {

    private PGQueryBuilder() {
    }

    public static PGSelect select(Object... sql) {
        PGSelectBuilder qb = new PGSelectBuilder("select");
        if (sql != null && sql.length > 0) {
            qb.literals(Arrays.asList(sql), " ", null, ",");
        }
        return qb;
    }

}
