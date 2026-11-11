package com.quickbite.dao;

import com.quickbite.model.Delivery;
import com.quickbite.util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** CRUD on deliveries.txt  (id|orderId|personId|personName|personPhone|status|assignedAt|deliveredAt) */
public class DeliveryDAO {
    private final FileHandler file;

    public DeliveryDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "deliveries.txt");
    }

    // CREATE
    public synchronized void add(Delivery d) throws IOException {
        int nextId = 1;
        for (Delivery x : getAll()) nextId = Math.max(nextId, x.getId() + 1);
        d.setId(nextId);
        file.appendLine(d.toFileLine());
    }

    // READ
    public synchronized List<Delivery> getAll() throws IOException {
        List<Delivery> list = new ArrayList<>();
        for (String line : file.readLines()) {
            String[] p = line.split("\\|", -1);
            if (p.length < 8) continue;
            try {
                list.add(new Delivery(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]),
                        p[3], p[4], p[5], p[6], p[7]));
            } catch (NumberFormatException ignored) { }
        }
        return list;
    }

    public synchronized Delivery getById(int id) throws IOException {
        for (Delivery d : getAll()) if (d.getId() == id) return d;
        return null;
    }

    /** The most recent delivery of an order (null when no rider was assigned yet). */
    public synchronized Delivery getByOrder(int orderId) throws IOException {
        Delivery found = null;
        for (Delivery d : getAll()) if (d.getOrderId() == orderId) found = d;
        return found;
    }

    /** The delivery of an order that is still in progress (Assigned / Picked up), or null. */
    public synchronized Delivery getActiveByOrder(int orderId) throws IOException {
        for (Delivery d : getAll()) if (d.getOrderId() == orderId && d.isActive()) return d;
        return null;
    }

    public synchronized boolean riderIsBusy(int personId) throws IOException {
        for (Delivery d : getAll()) if (d.getPersonId() == personId && d.isActive()) return true;
        return false;
    }

    // UPDATE
    public synchronized boolean update(Delivery updated) throws IOException {
        List<Delivery> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) { all.set(i, updated); saveAll(all); return true; }
        }
        return false;
    }

    // DELETE
    public synchronized boolean delete(int id) throws IOException {
        List<Delivery> all = getAll();
        boolean removed = all.removeIf(d -> d.getId() == id);
        if (removed) saveAll(all);
        return removed;
    }

    public synchronized void deleteByOrder(int orderId) throws IOException {
        List<Delivery> all = getAll();
        if (all.removeIf(d -> d.getOrderId() == orderId)) saveAll(all);
    }

    private void saveAll(List<Delivery> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Delivery d : list) lines.add(d.toFileLine());
        file.writeLines(lines);
    }
}
