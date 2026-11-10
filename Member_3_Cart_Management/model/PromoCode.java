package com.quickbite.model;

/** A discount code, e.g. WELCOME10 = 10% off when the cart is at least Rs. 1,000. */
public class PromoCode implements java.io.Serializable {
    private final String code;          // always upper case
    private final int percent;          // 1 - 100
    private final double minSubtotal;   // minimum cart value needed
    private final boolean active;

    public PromoCode(String code, int percent, double minSubtotal, boolean active) {
        this.code = code.trim().toUpperCase();
        this.percent = percent;
        this.minSubtotal = minSubtotal;
        this.active = active;
    }

    public boolean isApplicable(double subtotal) { return active && subtotal >= minSubtotal; }

    public double discountFor(double subtotal) {
        if (!isApplicable(subtotal)) return 0;
        return Math.round(subtotal * percent) / 100.0;
    }

    /** code|percent|minSubtotal|active */
    public String toFileLine() { return code + "|" + percent + "|" + minSubtotal + "|" + active; }

    public String getCode() { return code; }
    public int getPercent() { return percent; }
    public double getMinSubtotal() { return minSubtotal; }
    public boolean isActive() { return active; }
}
