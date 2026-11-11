package com.quickbite.dao;

import com.quickbite.model.DeliveryPerson;
import com.quickbite.util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** CRUD on drivers.txt  (id|name|phone|vehicle|available) */
public class DeliveryPersonDAO {
    private final FileHandler file;

    public DeliveryPersonDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "drivers.txt");
        if (file.readLines().isEmpty()) {
            add(new DeliveryPerson(0, "Kasun Perera", "0771112223", "Motorbike", true));
            add(new DeliveryPerson(0, "Nimali Silva", "0714445556", "Scooter", true));
        }
    }

    // CREATE
    public synchronized void add(DeliveryPerson d) throws IOException {
        int nextId = 1;
        for (DeliveryPerson x : getAll()) nextId = Math.max(nextId, x.getId() + 1);
        d.setId(nextId);
        file.appendLine(d.toFileLine());
    }

    // READ
    public synchronized List<DeliveryPerson> getAll() throws IOException {
        List<DeliveryPerson> list = new ArrayList<>();
        for (String line : file.readLines()) {
            String[] p = line.split("\\|", -1);
            if (p.length < 5) continue;
            try { list.add(new DeliveryPerson(Integer.parseInt(p[0]), p[1], p[2], p[3], Boolean.parseBoolean(p[4]))); }
            catch (NumberFormatException ignored) { }
        }
        return list;
    }

    public synchronized DeliveryPerson getById(int id) throws IOException {
        for (DeliveryPerson d : getAll()) if (d.getId() == id) return d;
        return null;
    }

    public synchronized List<DeliveryPerson> getAvailable() throws IOException {
        List<DeliveryPerson> list = new ArrayList<>();
        for (DeliveryPerson d : getAll()) if (d.isAvailable()) list.add(d);
        return list;
    }

    // UPDATE
    public synchronized boolean update(DeliveryPerson updated) throws IOException {
        List<DeliveryPerson> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) { all.set(i, updated); saveAll(all); return true; }
        }
        return false;
    }

    // DELETE
    public synchronized boolean delete(int id) throws IOException {
        List<DeliveryPerson> all = getAll();
        boolean removed = all.removeIf(d -> d.getId() == id);
        if (removed) saveAll(all);
        return removed;
    }

    private void saveAll(List<DeliveryPerson> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (DeliveryPerson d : list) lines.add(d.toFileLine());
        file.writeLines(lines);
    }
}
