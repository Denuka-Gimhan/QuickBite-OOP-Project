package com.quickbite.exception;

/**
 * Thrown when a promotional discount code is inactive, non-existent,
 * or the cart subtotal does not meet the minimum spend requirement.
 */
public class InvalidPromoException extends QuickBiteException {

    public InvalidPromoException(String message) {
        super(message);
    }

    public InvalidPromoException(String message, Throwable cause) {
        super(message, cause);
    }
}
