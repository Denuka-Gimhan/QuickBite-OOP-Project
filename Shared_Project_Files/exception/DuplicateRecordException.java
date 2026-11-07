package com.quickbite.exception;

/**
 * Thrown when attempting to add a record with a unique key that already exists
 * (e.g., duplicate username, email, promo code, or order rating).
 */
public class DuplicateRecordException extends QuickBiteException {

    public DuplicateRecordException(String message) {
        super(message);
    }

    public DuplicateRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
