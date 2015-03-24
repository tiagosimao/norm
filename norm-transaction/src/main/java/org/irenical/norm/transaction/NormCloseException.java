package org.irenical.norm.transaction;

public class NormCloseException extends NormException {

    private static final long serialVersionUID = 1L;

    public NormCloseException() {
	super();
    }

    public NormCloseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public NormCloseException(String message, Throwable cause) {
	super(message, cause);
    }

    public NormCloseException(String message) {
	super(message);
    }

    public NormCloseException(Throwable cause) {
	super(cause);
    }

}
