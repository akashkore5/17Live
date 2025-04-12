package org.live17.exception;

public class DeduplicationException extends RuntimeException {
    public DeduplicationException(String message) {
        super(message);
    }

    public DeduplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}