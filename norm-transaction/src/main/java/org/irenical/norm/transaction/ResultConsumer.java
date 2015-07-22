package org.irenical.norm.transaction;

import java.sql.SQLException;

@FunctionalInterface
public interface ResultConsumer<INPUT> {

    void accept(NormResult<INPUT> result) throws SQLException;

}
