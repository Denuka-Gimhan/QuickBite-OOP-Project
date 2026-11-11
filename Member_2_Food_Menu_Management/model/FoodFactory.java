package com.quickbite.model;

/** Creates the right Food subclass from a category name. */
public final class FoodFactory {
    private FoodFactory() {}

    public static Food create(int id, String name, String category, double price, String description, String emoji) {
        return create(id, name, category, price, description, emoji, true);
    }

    public static Food create(int id, String name, String category, double price, String description,
                              String emoji, boolean available) {
        Food f;
        switch (category == null ? "" : category) {
            case "Fast Food": f = new FastFood(id, name, price, description, emoji); break;
            case "Beverages": f = new Beverage(id, name, price, description, emoji); break;
            case "Desserts":  f = new Dessert(id, name, price, description, emoji); break;
            default:          f = new TraditionalFood(id, name, price, description, emoji);
        }
        f.setAvailable(available);
        return f;
    }
}
