package org.irenical.norm.transaction;

public class NormCommitException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NormCommitException() {
	super();
    }

    public NormCommitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public NormCommitException(String message, Throwable cause) {
	super(message, cause);
    }

    public NormCommitException(String message) {
	super(message);
    }

    public NormCommitException(Throwable cause) {
	super(cause);
    }

}
