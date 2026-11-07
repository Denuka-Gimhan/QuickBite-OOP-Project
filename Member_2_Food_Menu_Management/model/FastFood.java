package com.quickbite.model;

/** Inherits from Food and overrides the abstract methods. */
public class FastFood extends Food {
    public FastFood(int id, String name, double price, String description, String emoji) {
        super(id, name, price, description, emoji);
    }
    @Override public String getCategory() { return "Fast Food"; }
    @Override public int getPreparationTime() { return 15; }
    @Override public String getDetails() { return "Made fresh to order, ready in about 15 min"; }
    @Override public String getThemeColor() { return "#ffe0d6"; }
}
