package org.irenical.norm.transaction;

import java.sql.SQLException;

@FunctionalInterface
public interface NTOutputReader<INPUT, OUTPUT> {

    OUTPUT toOutput(NTContext<INPUT, OUTPUT> context) throws SQLException;

}
