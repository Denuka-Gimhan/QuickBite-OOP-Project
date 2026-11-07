package com.quickbite.model;

import java.util.List;

/** Money record of one order. Pending -> Paid (when delivered) -> Refunded, or Cancelled if the order was cancelled unpaid. */
public class Payment {
    public static final List<String> STATUSES = List.of("Pending", "Paid", "Refunded", "Cancelled");

    private int id;
    private final int orderId;
    private final String method;     // Cash on delivery / Card on delivery
    private final double amount;     // order total at the time of ordering
    private String status;
    private final String date;

    public Payment(int id, int orderId, String method, double amount, String status, String date) {
        this.id = id; this.orderId = orderId; this.method = method; this.amount = amount;
        this.status = status; this.date = date;
    }

    /** id|orderId|method|amount|status|date */
    public String toFileLine() {
        return id + "|" + orderId + "|" + method + "|" + amount + "|" + status + "|" + date;
    }

    public String getStatusClass() { return "pay-" + status.toLowerCase(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public String getMethod() { return method; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDate() { return date; }
}
