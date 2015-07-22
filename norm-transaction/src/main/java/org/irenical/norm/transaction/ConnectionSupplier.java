package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionSupplier {
    
    Connection get() throws SQLException;

}
