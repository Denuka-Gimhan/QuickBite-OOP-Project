package com.quickbite.dao;

import com.quickbite.model.Rating;
import com.quickbite.util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** CRUD on ratings.txt  (id|orderId|userId|username|stars|date|comment) */
public class RatingDAO {
    private final FileHandler file;

    public RatingDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "ratings.txt");
    }

    // CREATE (false when this order already has a rating)
    public synchronized boolean add(Rating r) throws IOException {
        if (getByOrder(r.getOrderId()) != null) return false;
        int nextId = 1;
        for (Rating x : getAll()) nextId = Math.max(nextId, x.getId() + 1);
        r.setId(nextId);
        file.appendLine(r.toFileLine());
        return true;
    }

    // READ
    public synchronized List<Rating> getAll() throws IOException {
        List<Rating> list = new ArrayList<>();
        for (String line : file.readLines()) {
            String[] p = line.split("\\|", 7);
            if (p.length < 7) continue;
            try {
                list.add(new Rating(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]),
                        p[3], Integer.parseInt(p[4]), p[6], p[5]));
            } catch (NumberFormatException ignored) { }
        }
        return list;
    }

    public synchronized Rating getByOrder(int orderId) throws IOException {
        for (Rating r : getAll()) if (r.getOrderId() == orderId) return r;
        return null;
    }

    public synchronized double getAverage() throws IOException {
        List<Rating> all = getAll();
        if (all.isEmpty()) return 0;
        double sum = 0;
        for (Rating r : all) sum += r.getStars();
        return sum / all.size();
    }

    // DELETE
    public synchronized boolean delete(int id) throws IOException {
        List<Rating> all = getAll();
        boolean removed = all.removeIf(r -> r.getId() == id);
        if (removed) saveAll(all);
        return removed;
    }

    public synchronized void deleteByOrder(int orderId) throws IOException {
        List<Rating> all = getAll();
        if (all.removeIf(r -> r.getOrderId() == orderId)) saveAll(all);
    }

    private void saveAll(List<Rating> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Rating r : list) lines.add(r.toFileLine());
        file.writeLines(lines);
    }
}
