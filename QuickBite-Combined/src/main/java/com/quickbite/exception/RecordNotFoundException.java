package com.quickbite.exception;

/**
 * Thrown when an expected record is not found in the persistent data store.
 */
public class RecordNotFoundException extends QuickBiteException {

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
