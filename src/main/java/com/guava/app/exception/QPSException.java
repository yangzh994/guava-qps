package com.guava.app.exception;

public class QPSException extends RuntimeException {

    public QPSException() {
        super();
    }

    public QPSException(String message) {
        super(message);
    }

    public QPSException(String message, Throwable cause) {
        super(message, cause);
    }

    public QPSException(Throwable cause) {
        super(cause);
    }

    protected QPSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
