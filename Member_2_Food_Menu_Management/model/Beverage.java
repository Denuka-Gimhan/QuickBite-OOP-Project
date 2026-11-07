package com.quickbite.model;

/** Inherits from Food and overrides the abstract methods. */
public class Beverage extends Food {
    public Beverage(int id, String name, double price, String description, String emoji) {
        super(id, name, price, description, emoji);
    }
    @Override public String getCategory() { return "Beverages"; }
    @Override public int getPreparationTime() { return 5; }
    @Override public String getDetails() { return "Served chilled or hot, ready in about 5 min"; }
    @Override public String getThemeColor() { return "#e3f3e0"; }
}
