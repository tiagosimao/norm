package org.irenical.norm.transaction;

import java.sql.SQLException;

@FunctionalInterface
public interface NormResultConsumer<INPUT> {

    void accept(NormResult<INPUT> result) throws SQLException;

}
