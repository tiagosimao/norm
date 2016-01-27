package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface NormConnectionSupplier {
    
    Connection get() throws SQLException;

}
