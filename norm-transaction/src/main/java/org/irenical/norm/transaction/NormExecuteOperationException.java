package org.irenical.norm.transaction;

public class NormExecuteOperationException extends NormException {

    private static final long serialVersionUID = 1L;

    public NormExecuteOperationException() {
	super();
    }

    public NormExecuteOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public NormExecuteOperationException(String message, Throwable cause) {
	super(message, cause);
    }

    public NormExecuteOperationException(String message) {
	super(message);
    }

    public NormExecuteOperationException(Throwable cause) {
	super(cause);
    }
    
}
