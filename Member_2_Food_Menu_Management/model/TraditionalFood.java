package com.quickbite.model;

/** Inherits from Food and overrides the abstract methods. */
public class TraditionalFood extends Food {
    public TraditionalFood(int id, String name, double price, String description, String emoji) {
        super(id, name, price, description, emoji);
    }
    @Override public String getCategory() { return "Traditional"; }
    @Override public int getPreparationTime() { return 25; }
    @Override public String getDetails() { return "Home-style Sri Lankan dish, ready in about 25 min"; }
    @Override public String getThemeColor() { return "#ffe8d1"; }
}
