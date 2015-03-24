package org.irenical.norm.transaction;

public class NormException extends Exception {

    private static final long serialVersionUID = 1L;

    public NormException() {
	super();
    }

    public NormException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public NormException(String message, Throwable cause) {
	super(message, cause);
    }

    public NormException(String message) {
	super(message);
    }

    public NormException(Throwable cause) {
	super(cause);
    }
    
}
