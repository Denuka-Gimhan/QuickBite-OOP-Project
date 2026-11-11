package com.quickbite.dao;

import com.quickbite.model.Delivery;
import com.quickbite.util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * DAO = Data Access Object.
 * This class only talks to the text file "deliveries.txt".
 * One line of the file looks like:
 * id|orderId|personId|personName|personPhone|status|assignedAt|deliveredAt
 * The '|' is the separator.
 */

/** CRUD on deliveries.text  (id|orderId|personId|personName|personPhone|status|assignedAt|deliveredAt) */
// FileHandler is a helper class that reads and writes the text file.
public class DeliveryDAO {
    private final FileHandler file;

    // Constructor: builds the file path. Example: data/deliveries.txt
    public DeliveryDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "deliveries.txt");
    }

    // ---------- CREATE ----------
    // Add a new delivery to the file.
    // We auto-generate the ID: take the biggest existing ID and add 1.

    public synchronized void add(Delivery d) throws IOException {
        int nextId = 1;
        for (Delivery x : getAll()) nextId = Math.max(nextId, x.getId() + 1);
        d.setId(nextId);
        file.appendLine(d.toFileLine());
    }

    // ---------- READ ----------
    // Read the whole file and return a list of Delivery objects.
    // We split each line by '|'. If a line is broken, we skip it.

    public synchronized List<Delivery> getAll() throws IOException {
        List<Delivery> list = new ArrayList<>();
        for (String line : file.readLines()) {
            String[] p = line.split("\\|", -1); // -1 = keep empty parts
            if (p.length < 8) continue;         // broken line → skip
            try {
                list.add(new Delivery(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]),
                        p[3], p[4], p[5], p[6], p[7]));
            } catch (NumberFormatException ignored) { }  // bad number → skip
        }
        return list;
    }
    // Find one delivery by its own ID. Returns null if not found.
    public synchronized Delivery getById(int id) throws IOException {
        for (Delivery d : getAll()) if (d.getId() == id) return d;
        return null;
    }

    /** The most recent delivery of an order (null when no rider was assigned yet). */
    // Get the newest delivery record for a given order.
    // We keep looping, so the LAST match wins = most recent.
    public synchronized Delivery getByOrder(int orderId) throws IOException {
        Delivery found = null;
        for (Delivery d : getAll()) if (d.getOrderId() == orderId) found = d;
        return found;
    }

    /** The delivery of an order that is still
     *  in progress (Assigned / Picked up), or null. */
    // Get the delivery of an order that is still running
    // (status = "Assigned" or "Picked up"). Returns null if finished.
    public synchronized Delivery getActiveByOrder(int orderId) throws IOException {
        for (Delivery d : getAll()) if (d.getOrderId() == orderId && d.isActive()) return d;
        return null;
    }

    // Check if this rider already has an active delivery.
    // Used to stop assigning the same rider to two orders at once.
    public synchronized boolean riderIsBusy(int personId) throws IOException {
        for (Delivery d : getAll()) if (d.getPersonId() == personId && d.isActive()) return true;
        return false;
    }


    // ---------- UPDATE ----------
    // Replace one delivery with a new version (same ID).
    // We must rewrite the whole file because text files can't be edited in place easily.

    public synchronized boolean update(Delivery updated) throws IOException {
        List<Delivery> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) { all.set(i, updated); saveAll(all); return true; }
        }
        return false;
    }


    // ---------- DELETE ----------
    // Delete one delivery by ID. Rewrite file after removing.
    public synchronized boolean delete(int id) throws IOException {
        List<Delivery> all = getAll();
        boolean removed = all.removeIf(d -> d.getId() == id);
        if (removed) saveAll(all);     // rewrite file
        return removed;
    }


    // Delete ALL delivery records of an order (used when order is cancelled).
    public synchronized void deleteByOrder(int orderId) throws IOException {
        List<Delivery> all = getAll();
        if (all.removeIf(d -> d.getOrderId() == orderId)) saveAll(all);
    }

    // Helper: write the whole list back to the file.
    // We convert each Delivery to a line first (toFileLine()).
    private void saveAll(List<Delivery> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Delivery d : list) lines.add(d.toFileLine());
        file.writeLines(lines);
    }
}
