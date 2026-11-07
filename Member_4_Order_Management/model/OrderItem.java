package com.quickbite.model;

/** One line of an order (a snapshot of name and price at the time of ordering). */
public class OrderItem {
    private final String name;
    private final int qty;
    private final double price;
    private String emoji = "";
    private String imageFile = "";

    public OrderItem(String name, int qty, double price) {
        this(name, qty, price, "", "");
    }

    public OrderItem(String name, int qty, double price, String emoji, String imageFile) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.emoji = emoji == null ? "" : emoji;
        this.imageFile = imageFile == null ? "" : imageFile;
    }

    public String getName() { return name; }
    public int getQty() { return qty; }
    public double getPrice() { return price; }
    public String getEmoji() { return emoji; }
    public String getImageFile() { return imageFile; }
    public double getLineTotal() { return price * qty; }
}
