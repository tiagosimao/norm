package org.irenical.norm.query.postgresql;

public class PGQueryBuilder {

    private PGQueryBuilder() {
    }

    public static PGSelect select() {
        return select(null);
    }

    public static PGSelect select(String sql) {
        PGSelectBuilder qb = new PGSelectBuilder("select");
        if (sql != null) {
            qb.literal(sql);
        }
        return qb;
    }

}
