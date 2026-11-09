package com.quickbite.dao;

import com.quickbite.model.Food;
import com.quickbite.model.FoodFactory;
import com.quickbite.util.FileHandler;
import com.quickbite.util.QuickSort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** CRUD on foods.txt.  Create=add, Read=getAll/getById/search, Update=update, Delete=delete. */
public class FoodDAO {
    private final FileHandler file;

    public FoodDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "foods.txt");
        if (file.readLines().isEmpty()) seed();
    }

    // ---- CREATE ----
    public synchronized void add(Food food) throws IOException {
        int nextId = 1;
        for (Food f : getAll()) nextId = Math.max(nextId, f.getId() + 1);
        food.setId(nextId);
        file.appendLine(food.toFileLine());
    }

    // ---- READ ----
    public synchronized List<Food> getAll() throws IOException {
        List<Food> list = new ArrayList<>();
        for (String line : file.readLines()) {
            Food f = parse(line);
            if (f != null) list.add(f);
        }
        return list;
    }

    public synchronized Food getById(int id) throws IOException {
        for (Food f : getAll()) if (f.getId() == id) return f;
        return null;
    }

    /** Search by name/description text and/or category. Empty values are ignored. */
    public synchronized List<Food> search(String text, String category) throws IOException {
        return search(text, category, null);
    }

    /** sort = "price_asc" | "price_desc" | anything else (file order). Sorting uses our own QuickSort. */
    public synchronized List<Food> search(String text, String category, String sort) throws IOException {
        String q = text == null ? "" : text.trim().toLowerCase();
        List<Food> result = new ArrayList<>();
        for (Food f : getAll()) {
            boolean catOk = category == null || category.isBlank() || f.getCategory().equals(category);
            boolean textOk = q.isEmpty() || (f.getName() + " " + f.getDescription()).toLowerCase().contains(q);
            if (catOk && textOk) result.add(f);
        }
        Comparator<Food> byPrice = Comparator.comparingDouble(Food::getPrice).thenComparing(Food::getName);
        if ("price_asc".equals(sort)) QuickSort.sort(result, byPrice);
        else if ("price_desc".equals(sort)) QuickSort.sort(result, byPrice.reversed());
        return result;
    }

    // ---- UPDATE ----
    public synchronized boolean update(Food updated) throws IOException {
        List<Food> all = getAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) { all.set(i, updated); found = true; break; }
        }
        if (found) saveAll(all);
        return found;
    }

    // ---- DELETE ----
    public synchronized boolean delete(int id) throws IOException {
        List<Food> all = getAll();
        boolean removed = all.removeIf(f -> f.getId() == id);
        if (removed) saveAll(all);
        return removed;
    }

    private void saveAll(List<Food> foods) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Food f : foods) lines.add(f.toFileLine());
        file.writeLines(lines);
    }

    private Food parse(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 6) return null;
        try {
            boolean available = p.length < 7 || !"false".equals(p[6]);   // old files have no 7th column
            Food f = FoodFactory.create(Integer.parseInt(p[0]), p[1], p[2], Double.parseDouble(p[3]), p[4], p[5], available);
            if (p.length > 7) f.setImageFile(p[7]);   // old files have no 8th column
            return f;
        } catch (NumberFormatException e) {
            return null;   // skip damaged line
        }
    }

    private void seed() throws IOException {
        add(FoodFactory.create(0, "Chicken Kottu", "Traditional", 1250, "Chopped roti with chicken, egg and spicy gravy.", "🥘"));
        add(FoodFactory.create(0, "Rice & Curry", "Traditional", 950, "Red rice with dhal, fish curry and three sambols.", "🍛"));
        add(FoodFactory.create(0, "Cheese Burger", "Fast Food", 1100, "Beef patty, cheddar, lettuce and house sauce.", "🍔"));
        add(FoodFactory.create(0, "Iced Milo", "Beverages", 350, "Cold, chocolatey and extra thick.", "🥤"));
        add(FoodFactory.create(0, "King Coconut", "Beverages", 300, "Fresh thambili, served chilled.", "🥥"));
        add(FoodFactory.create(0, "Watalappan", "Desserts", 450, "Coconut custard with jaggery and cardamom.", "🍮"));
    }
}
