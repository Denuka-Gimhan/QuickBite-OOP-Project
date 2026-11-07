package com.quickbite.model;

import java.util.List;

public final class OrderFactory {
    private OrderFactory() {}

    public static Order create(String type, int id, int userId, String username, String status, String date,
                               String address, String phone, String payment, List<OrderItem> items) {
        return "EXPRESS".equals(type)
                ? new ExpressOrder(id, userId, username, status, date, address, phone, payment, items)
                : new StandardOrder(id, userId, username, status, date, address, phone, payment, items);
    }
}
