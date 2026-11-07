package com.quickbite.model;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;

/** Base order. Delivery fee and ETA depend on the order type (polymorphism). */
public abstract class Order {
    public static final List<String> STATUSES = List.of("Pending", "Preparing", "On the way", "Delivered", "Cancelled");

    private int id;
    private int userId;
    private String username;
    private String status;
    private String date;
    private String address;
    private String phone;
    private String payment;
    private final List<OrderItem> items;
    private String promoCode = "";
    private double discount;

    protected Order(int id, int userId, String username, String status, String date,
                    String address, String phone, String payment, List<OrderItem> items) {
        this.id = id; this.userId = userId; this.username = username; this.status = status; this.date = date;
        this.address = address; this.phone = phone; this.payment = payment; this.items = items;
    }

    public abstract String getType();
    public abstract String getLabel();
    public abstract double getDeliveryFee();
    public abstract String getEta();

    public double getSubtotal() {
        double t = 0;
        for (OrderItem i : items) t += i.getLineTotal();
        return t;
    }
    /** Subtotal - promo discount + delivery fee (never below the delivery fee). */
    public double getTotal() { return Math.max(0, getSubtotal() - discount) + getDeliveryFee(); }
    public String getStatusClass() { return "status-" + status.toLowerCase().replace(' ', '-'); }
    public boolean isCancellable() { return "Pending".equals(status); }

    /** id|userId|username|type|status|date|address|phone|payment|name:qty:price;name:qty:price|promoCode|discount
     *  Item names are URL-encoded so ':' ';' '|' in a food name can never break the file. */
    public String toFileLine() {
        StringJoiner sj = new StringJoiner(";");
        for (OrderItem i : items) {
            String encodedName = URLEncoder.encode(i.getName(), StandardCharsets.UTF_8);
            String encodedEmoji = URLEncoder.encode(i.getEmoji() == null ? "" : i.getEmoji(), StandardCharsets.UTF_8);
            String encodedImg = URLEncoder.encode(i.getImageFile() == null ? "" : i.getImageFile(), StandardCharsets.UTF_8);
            sj.add(encodedName + ":" + i.getQty() + ":" + i.getPrice() + ":" + encodedEmoji + ":" + encodedImg);
        }
        return String.join("|", String.valueOf(id), String.valueOf(userId), username, getType(), status, date,
                address, phone, payment, sj.toString(), promoCode, String.valueOf(discount));
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDate() { return date; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getPayment() { return payment; }
    public List<OrderItem> getItems() { return items; }
    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode == null ? "" : promoCode; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
}
