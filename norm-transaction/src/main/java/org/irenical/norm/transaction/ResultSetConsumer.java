package org.irenical.norm.transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetConsumer {

    void accept(ResultSet rs) throws SQLException;

}
