package com.quickbite.model;

/** Inherits from Food and overrides the abstract methods. */
public class Dessert extends Food {
    public Dessert(int id, String name, double price, String description, String emoji) {
        super(id, name, price, description, emoji);
    }
    @Override public String getCategory() { return "Desserts"; }
    @Override public int getPreparationTime() { return 10; }
    @Override public String getDetails() { return "Sweet treat, ready in about 10 min"; }
    @Override public String getThemeColor() { return "#ffdce8"; }
}
