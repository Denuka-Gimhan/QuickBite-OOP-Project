package com.quickbite.dao;

import com.quickbite.model.PromoCode;
import com.quickbite.util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** CRUD on promocodes.txt  (code|percent|minSubtotal|active) */
public class PromoCodeDAO {
    private final FileHandler file;

    public PromoCodeDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "promocodes.txt");
        if (file.readLines().isEmpty()) {
            add(new PromoCode("WELCOME10", 10, 1000, true));
            add(new PromoCode("QUICK20", 20, 3000, true));
        }
    }

    // CREATE (false when the code already exists)
    public synchronized boolean add(PromoCode p) throws IOException {
        if (findByCode(p.getCode()) != null) return false;
        file.appendLine(p.toFileLine());
        return true;
    }

    // READ
    public synchronized List<PromoCode> getAll() throws IOException {
        List<PromoCode> list = new ArrayList<>();
        for (String line : file.readLines()) {
            String[] x = line.split("\\|", -1);
            if (x.length < 4) continue;
            try { list.add(new PromoCode(x[0], Integer.parseInt(x[1]), Double.parseDouble(x[2]), Boolean.parseBoolean(x[3]))); }
            catch (NumberFormatException ignored) { }
        }
        return list;
    }

    public synchronized PromoCode findByCode(String code) throws IOException {
        if (code == null) return null;
        for (PromoCode p : getAll()) if (p.getCode().equalsIgnoreCase(code.trim())) return p;
        return null;
    }

    // UPDATE
    public synchronized boolean update(PromoCode updated) throws IOException {
        List<PromoCode> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getCode().equals(updated.getCode())) { all.set(i, updated); saveAll(all); return true; }
        }
        return false;
    }

    // DELETE
    public synchronized boolean delete(String code) throws IOException {
        List<PromoCode> all = getAll();
        boolean removed = all.removeIf(p -> p.getCode().equalsIgnoreCase(code));
        if (removed) saveAll(all);
        return removed;
    }

    private void saveAll(List<PromoCode> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (PromoCode p : list) lines.add(p.toFileLine());
        file.writeLines(lines);
    }
}
