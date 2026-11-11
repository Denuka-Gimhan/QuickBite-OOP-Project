package com.quickbite.dao;

import com.quickbite.model.Order;
import com.quickbite.model.OrderFactory;
import com.quickbite.model.OrderItem;
import com.quickbite.util.FileHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** CRUD on orders.txt */
public class OrderDAO {
    private final FileHandler file;

    public OrderDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "orders.txt");
    }

    // CREATE
    public synchronized void add(Order o) throws IOException {
        int nextId = 1001;
        for (Order x : getAll()) nextId = Math.max(nextId, x.getId() + 1);
        o.setId(nextId);
        file.appendLine(o.toFileLine());
    }

    // READ
    public synchronized List<Order> getAll() throws IOException {
        List<Order> list = new ArrayList<>();
        for (String line : file.readLines()) {
            Order o = parse(line);
            if (o != null) list.add(o);
        }
        return list;
    }

    public synchronized Order getById(int id) throws IOException {
        for (Order o : getAll()) if (o.getId() == id) return o;
        return null;
    }

    public synchronized List<Order> getByUser(int userId) throws IOException {
        List<Order> list = new ArrayList<>();
        for (Order o : getAll()) if (o.getUserId() == userId) list.add(o);
        return list;
    }

    // UPDATE (status changes / cancel)
    public synchronized boolean update(Order updated) throws IOException {
        List<Order> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) { all.set(i, updated); saveAll(all); return true; }
        }
        return false;
    }

    // DELETE
    public synchronized boolean delete(int id) throws IOException {
        List<Order> all = getAll();
        boolean removed = all.removeIf(o -> o.getId() == id);
        if (removed) saveAll(all);
        return removed;
    }

    private void saveAll(List<Order> orders) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Order o : orders) lines.add(o.toFileLine());
        file.writeLines(lines);
    }

    private Order parse(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 10) return null;
        try {
            List<OrderItem> items = new ArrayList<>();
            for (String part : p[9].split(";")) {
                String[] f = part.split(":");
                if (f.length >= 3) {
                    String name = URLDecoder.decode(f[0], StandardCharsets.UTF_8);
                    int qty = Integer.parseInt(f[1]);
                    double price = Double.parseDouble(f[2]);
                    String emoji = f.length > 3 ? URLDecoder.decode(f[3], StandardCharsets.UTF_8) : "";
                    String imageFile = f.length > 4 ? URLDecoder.decode(f[4], StandardCharsets.UTF_8) : "";
                    items.add(new OrderItem(name, qty, price, emoji, imageFile));
                }
            }
            Order o = OrderFactory.create(p[3], Integer.parseInt(p[0]), Integer.parseInt(p[1]), p[2], p[4], p[5],
                    p[6], p[7], p[8], items);
            if (p.length >= 12) {                     // promo columns (older files do not have them)
                o.setPromoCode(p[10]);
                o.setDiscount(Double.parseDouble(p[11]));
            }
            return o;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
