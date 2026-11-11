package com.quickbite.model;

import java.util.List;

// Standard delivery: Rs. 250, free for orders of Rs. 5,000 or more.
public class StandardOrder extends Order {
    public StandardOrder(int id, int userId, String username, String status, String date,
                         String address, String phone, String payment, List<OrderItem> items) {
        super(id, userId, username, status, date, address, phone, payment, items);
    }
    @Override public String getType() { return "STANDARD"; }
    @Override public String getLabel() { return "Standard"; }
    @Override public double getDeliveryFee() { return getSubtotal() >= 5000 ? 0 : 250; }
    @Override public String getEta() { return "40-60 min"; }
}
