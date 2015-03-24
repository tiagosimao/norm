package org.irenical.norm.transaction;

public class NormResultException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NormResultException() {
		super();
	}

	public NormResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NormResultException(String message, Throwable cause) {
		super(message, cause);
	}

	public NormResultException(String message) {
		super(message);
	}

	public NormResultException(Throwable cause) {
		super(cause);
	}

}
