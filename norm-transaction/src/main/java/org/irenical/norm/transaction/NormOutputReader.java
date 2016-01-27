package org.irenical.norm.transaction;

import java.sql.SQLException;

@FunctionalInterface
public interface NormOutputReader<INPUT, OUTPUT> {

  OUTPUT toOutput(NormContext<INPUT, OUTPUT> context) throws SQLException;

}
