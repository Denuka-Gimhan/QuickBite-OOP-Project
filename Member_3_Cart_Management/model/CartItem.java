package com.quickbite.model;

public class CartItem implements java.io.Serializable {
    private final int foodId;
    private String name;
    private double price;
    private String emoji;
    private String imageFile = "";
    private int qty;

    public CartItem(int foodId, String name, double price, String emoji, int qty) {
        this.foodId = foodId; this.name = name; this.price = price; this.emoji = emoji; this.qty = qty;
    }

    public CartItem(int foodId, String name, double price, String emoji, String imageFile, int qty) {
        this(foodId, name, price, emoji, qty);
        this.imageFile = imageFile == null ? "" : imageFile;
    }

    /** Copies the latest name / price / emoji from the menu. Returns true when the price changed. */
    public boolean refreshFrom(Food f) {
        boolean priceChanged = Double.compare(price, f.getPrice()) != 0;
        this.name = f.getName(); this.price = f.getPrice(); this.emoji = f.getEmoji(); this.imageFile = f.getImageFile();
        return priceChanged;
    }

    public int getFoodId() { return foodId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getEmoji() { return emoji; }
    public String getImageFile() { return imageFile; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public double getLineTotal() { return price * qty; }
}
