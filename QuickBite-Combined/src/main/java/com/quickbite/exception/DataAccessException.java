package com.quickbite.exception;

/**
 * Wraps low-level I/O errors occurring during file data persistence.
 */
public class DataAccessException extends QuickBiteException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
