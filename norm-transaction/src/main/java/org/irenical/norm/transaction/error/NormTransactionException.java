package org.irenical.norm.transaction.error;

public class NormTransactionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NormTransactionException() {
        super();
    }

    public NormTransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NormTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NormTransactionException(String message) {
        super(message);
    }

    public NormTransactionException(Throwable cause) {
        super(cause);
    }

}
