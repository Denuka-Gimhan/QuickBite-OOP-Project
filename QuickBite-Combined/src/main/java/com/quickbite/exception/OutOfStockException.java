package com.quickbite.exception;

/**
 * Thrown when an operation is attempted on a food item that is out of stock.
 */
public class OutOfStockException extends QuickBiteException {

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
