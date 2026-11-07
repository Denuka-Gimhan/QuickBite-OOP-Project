package com.quickbite.exception;

/**
 * Base checked exception for the QuickBite application.
 * Demonstrates OOP Concept #7: Error Handling (Custom Exception Hierarchy)
 * and Concept #1: Constructors with chaining via super(...).
 */
public class QuickBiteException extends Exception {

    public QuickBiteException(String message) {
        super(message);
    }

    public QuickBiteException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuickBiteException(Throwable cause) {
        super(cause);
    }
}
