package com.quickbite.exception;

/**
 * Thrown when user authentication fails due to incorrect username, email, or password.
 */
public class InvalidCredentialsException extends QuickBiteException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
