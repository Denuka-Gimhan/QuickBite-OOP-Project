package com.quickbite.model;

import java.util.List;

// Express delivery: always Rs. 450, faster.
public class ExpressOrder extends Order {
    public ExpressOrder(int id, int userId, String username, String status, String date,
                        String address, String phone, String payment, List<OrderItem> items) {
        super(id, userId, username, status, date, address, phone, payment, items);
    }
    @Override public String getType() {
        return "EXPRESS"; }
    @Override public String getLabel() {
        return "Express"; }
    @Override public double getDeliveryFee() {
        return 450; }
    @Override public String getEta() {
        return "20 - 30 min"; }
}
