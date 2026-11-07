package com.quickbite.exception;

/**
 * Thrown when entity attributes fail business validation rules
 * (e.g., negative prices, empty names, invalid email or phone formats).
 */
public class ValidationException extends QuickBiteException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
