package org.irenical.norm.transaction.error;

/**
 * Created by tgsimao on 30/07/14.
 */
public class NormTransactionBeginException extends RuntimeException {

	private static final long serialVersionUID = 6062522651820419695L;

	public NormTransactionBeginException() {
    }

    public NormTransactionBeginException(String message) {
        super(message);
    }

    public NormTransactionBeginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NormTransactionBeginException(Throwable cause) {
        super(cause);
    }

    public NormTransactionBeginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
