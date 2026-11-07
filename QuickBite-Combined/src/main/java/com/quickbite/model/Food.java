package com.quickbite.model;

import java.util.List;

/** Base class: Encapsulation (private fields + getters/setters). Abstract methods enable Polymorphism. */
public abstract class Food {
    public static final List<String> CATEGORIES = List.of("Fast Food", "Beverages", "Desserts", "Traditional");

    private int id;
    private String name;
    private double price;
    private String description;
    private String emoji;
    private boolean available = true;   // false = "Out of stock"
    private String imageFile = "";      // uploaded photo file name (empty = show the emoji)

    protected Food(int id, String name, double price, String description, String emoji) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.emoji = emoji;
    }

    // Each subclass answers these in its own way (polymorphism)
    public abstract String getCategory();
    public abstract int getPreparationTime();   // minutes
    public abstract String getDetails();
    public abstract String getThemeColor();     // card background colour

    /** One line in foods.txt:  id|name|category|price|description|emoji|available|imageFile */
    public String toFileLine() {
        return id + "|" + name + "|" + getCategory() + "|" + price + "|" + description + "|" + emoji + "|" + available + "|" + imageFile;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getImageFile() { return imageFile; }
    public void setImageFile(String imageFile) { this.imageFile = imageFile == null ? "" : imageFile; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
}
