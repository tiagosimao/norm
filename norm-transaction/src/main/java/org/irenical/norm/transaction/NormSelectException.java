package org.irenical.norm.transaction;

public class NormSelectException extends NormExecuteOperationException {

    private static final long serialVersionUID = 1L;

    public NormSelectException() {
	super();
    }

    public NormSelectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public NormSelectException(String message, Throwable cause) {
	super(message, cause);
    }

    public NormSelectException(String message) {
	super(message);
    }

    public NormSelectException(Throwable cause) {
	super(cause);
    }
    
}
