package org.irenical.norm;

import java.sql.SQLException;


@FunctionalInterface
public interface NormRowReader<RETURN> {
	
	RETURN readRow(NormResult row) throws SQLException;

}
