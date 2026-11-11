package com.quickbite.model;

/** An invoice for one order = the order lines + its payment record. Built on demand, never stored. */
public class Bill {
    private final Order order;
    private final Payment payment;   // may be null for very old orders

    public Bill(Order order, Payment payment) { this.order = order; this.payment = payment; }

    public String getInvoiceNo() { return "INV-" + order.getId(); }
    public Order getOrder() { return order; }
    public Payment getPayment() { return payment; }
    public String getPaymentStatus() { return payment == null ? "Pending" : payment.getStatus(); }
    public boolean isPaid() { return "Paid".equals(getPaymentStatus()); }

    /** What the customer still has to pay. */
    public double getAmountDue() {
        String s = getPaymentStatus();
        return ("Paid".equals(s) || "Cancelled".equals(s) || "Refunded".equals(s)) ? 0 : order.getTotal();
    }
}
