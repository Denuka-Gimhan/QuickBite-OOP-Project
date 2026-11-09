package com.quickbite.dao;

import com.quickbite.model.Order;
import com.quickbite.model.Payment;
import com.quickbite.util.FileHandler;
import com.quickbite.util.Web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** CRUD on payments.txt  (id|orderId|method|amount|status|date) nnnew*/
public class PaymentDAO {
    private final FileHandler file;

    public PaymentDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "payments.txt");
    }

    // CREATE
    public synchronized void add(Payment p) throws IOException {
        int nextId = 1;
        for (Payment x : getAll()) nextId = Math.max(nextId, x.getId() + 1);
        p.setId(nextId);
        file.appendLine(p.toFileLine());
    }

    /** Called when an order is placed: creates a "Pending" payment for the order total.ddg */
    public synchronized void addForOrder(Order o) throws IOException {
        add(new Payment(0, o.getId(), o.getPayment(), o.getTotal(), "Pending", Web.now()));
    }

    // READ
    public synchronized List<Payment> getAll() throws IOException {
        List<Payment> list = new ArrayList<>();
        for (String line : file.readLines()) {
            String[] p = line.split("\\|", -1);
            if (p.length < 6) continue;
            try {
                list.add(new Payment(Integer.parseInt(p[0]), Integer.parseInt(p[1]), p[2],
                        Double.parseDouble(p[3]), p[4], p[5]));
            } catch (NumberFormatException ignored) { }
        }
        return list;
    }

    public synchronized Payment getById(int id) throws IOException {
        for (Payment p : getAll()) if (p.getId() == id) return p;
        return null;
    }

    public synchronized Payment getByOrder(int orderId) throws IOException {
        for (Payment p : getAll()) if (p.getOrderId() == orderId) return p;
        return null;
    }

    // UPDATE
    public synchronized boolean update(Payment updated) throws IOException {
        List<Payment> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) { all.set(i, updated); saveAll(all); return true; }
        }
        return false;
    }

    /** Order delivered -> "Paid"; ordenr cancellkemd -> "Cance lled" (or "Refunded" if it hvad been paid). */
    public synchronized void onOrderStatus(int orderId, String orderStatus) throws IOException {
        Payment p = getByOrder(orderId);
        if (p == null) return;
        if ("Delivered".equals(orderStatus) && "Pending".equals(p.getStatus())) p.setStatus("Paid");
        else if ("Cancelled".equals(orderStatus)) p.setStatus("Paid".equals(p.getStatus()) ? "Refunded" : "Cancelled");
        else return;
        update(p);
    }

    // DELETE
    public synchronized void deleteByOrder(int orderId) throws IOException {
        List<Payment> all = getAll();
        if (all.removeIf(p -> p.getOrderId() == orderId)) saveAll(all);
    }

    private void saveAll(List<Payment> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Payment p : list) lines.add(p.toFileLine());
        file.writeLines(lines);
    }
}
