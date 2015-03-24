package org.irenical.norm.transaction;

import java.sql.Connection;

@FunctionalInterface
public interface NormConnectionPoller<EXCEPTION extends Throwable> {
	
	Connection poll() throws EXCEPTION;

}
