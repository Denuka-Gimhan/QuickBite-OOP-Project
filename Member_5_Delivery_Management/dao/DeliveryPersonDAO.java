package com.quickbite.dao;

import com.quickbite.model.DeliveryPerson;
import com.quickbite.util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** CRUD on drivers.txt  (id|name|phone|vehicle|available) */

/**
 * DAO = Data Access Object.
 * This class only talks to the text file "drivers.txt".
 * One line of the file looks like:
 * id|name|phone|vehicle|available
 * The '|' is the separator.
 */
public class DeliveryPersonDAO {
    // FileHandler is a helper class that reads and writes the text file.
    private final FileHandler file;


    // Constructor: builds the file path (data/drivers.txt).
    // If the file is empty (first time app runs), we add 2 default riders
    // so the system is not empty at the start.
    public DeliveryPersonDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "drivers.txt");
        if (file.readLines().isEmpty()) {
            add(new DeliveryPerson(0, "Kasun Perera", "0771112223", "Motorbike", true));
            add(new DeliveryPerson(0, "Nimali Silva", "0714445556", "Scooter", true));
        }
    }

    // CREATE
    // Add a new rider. We auto-generate the ID: max existing ID + 1.
    public synchronized void add(DeliveryPerson d) throws IOException {
        int nextId = 1;
        for (DeliveryPerson x : getAll()) nextId = Math.max(nextId, x.getId() + 1);
        d.setId(nextId);
        file.appendLine(d.toFileLine());
    }

    // READ
    // Read whole file → return list of DeliveryPerson objects.
    // Split each line by '|'. Skip broken lines. Convert last field to boolean.

    public synchronized List<DeliveryPerson> getAll() throws IOException {
        List<DeliveryPerson> list = new ArrayList<>();
        for (String line : file.readLines()) {
            String[] p = line.split("\\|", -1);   // -1 = keep empty parts
            if (p.length < 5) continue;           // broken line → skip
            try { list.add(new DeliveryPerson(
                    Integer.parseInt(p[0]),  // id
                    p[1],                      // name
                    p[2],                   // phone
                    p[3],                   // vehicle
                    Boolean.parseBoolean(p[4]))); }  // available
            catch (NumberFormatException ignored) { }   // bad id → skip
        }
        return list;
    }

    // Find one rider by ID. Returns null if not found.
    public synchronized DeliveryPerson getById(int id) throws IOException {
        for (DeliveryPerson d : getAll()) if (d.getId() == id) return d;
        return null;
    }

    // Return only riders whose 'available' flag is true.
    // Used when assigning a rider to an order.
    public synchronized List<DeliveryPerson> getAvailable() throws IOException {
        List<DeliveryPerson> list = new ArrayList<>();
        for (DeliveryPerson d : getAll()) if (d.isAvailable()) list.add(d);
        return list;
    }

    // UPDATE
    // Replace one rider with a new version (same ID).
    // Text file can't be edited in place, so we rewrite the whole file.
    public synchronized boolean update(DeliveryPerson updated) throws IOException {
        List<DeliveryPerson> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) { all.set(i, updated);
                saveAll(all); return true; }  // rewrite file
        }
        return false;
    }

    // DELETE
    // Delete one rider by ID. Rewrite file after removing.
    public synchronized boolean delete(int id) throws IOException {
        List<DeliveryPerson> all = getAll();
        boolean removed = all.removeIf(d -> d.getId() == id);
        if (removed) saveAll(all);
        return removed;
    }

    // Helper: write the whole list back to the file.
    // We convert each DeliveryPerson to a line first (toFileLine()).
    private void saveAll(List<DeliveryPerson> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (DeliveryPerson d : list) lines.add(d.toFileLine());
        file.writeLines(lines);
    }
}
