package com.quickbite.dao;

import com.quickbite.model.*;
import com.quickbite.util.FileHandler;
import com.quickbite.util.PasswordUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** CRUD on users.txt */
public class UserDAO {
    private final FileHandler file;

    public UserDAO(String dataDir) throws IOException {
        file = new FileHandler(dataDir, "users.txt");
        if (file.readLines().isEmpty()) {
            add(new AdminUser(0, "admin", PasswordUtil.hash("admin123"), "admin@quickbite.lk", "0770000000", "Head office"));
        }
    }

    // CREATE (returns false if the username or the email is already taken)
    public synchronized boolean add(User u) throws IOException {
        if (findByUsername(u.getUsername()) != null || findByEmail(u.getEmail()) != null) return false;
        int nextId = 1;
        for (User x : getAll()) nextId = Math.max(nextId, x.getId() + 1);
        u.setId(nextId);
        file.appendLine(u.toFileLine());
        return true;
    }

    // READ
    public synchronized List<User> getAll() throws IOException {
        List<User> list = new ArrayList<>();
        for (String line : file.readLines()) {
            String[] p = line.split("\\|", -1);
            if (p.length < 7) continue;
            try { list.add(UserFactory.create(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4], p[5], p[6])); }
            catch (NumberFormatException ignored) { }
        }
        return list;
    }

    public synchronized User getById(int id) throws IOException {
        for (User u : getAll()) if (u.getId() == id) return u;
        return null;
    }

    public synchronized User findByUsername(String username) throws IOException {
        for (User u : getAll()) if (u.getUsername().equalsIgnoreCase(username)) return u;
        return null;
    }

    public synchronized User findByEmail(String email) throws IOException {
        if (email == null || email.isBlank()) return null;
        for (User u : getAll()) if (u.getEmail().equalsIgnoreCase(email.trim())) return u;
        return null;
    }

    /** Returns the user when (username OR email) + password match, otherwise null. */
    public synchronized User authenticate(String usernameOrEmail, String password) throws IOException {
        String login = usernameOrEmail == null ? "" : usernameOrEmail.trim();
        User u = login.contains("@") ? findByEmail(login) : findByUsername(login);
        return (u != null && PasswordUtil.matches(password, u.getPasswordHash())) ? u : null;
    }

    public synchronized List<User> search(String text) throws IOException {
        String q = text == null ? "" : text.trim().toLowerCase();
        List<User> result = new ArrayList<>();
        for (User u : getAll()) {
            if (q.isEmpty() || (u.getUsername() + " " + u.getEmail()).toLowerCase().contains(q)) result.add(u);
        }
        return result;
    }

    public synchronized int countAdmins() throws IOException {
        int n = 0;
        for (User u : getAll()) if (u.isAdmin()) n++;
        return n;
    }

    // UPDATE
    public synchronized boolean update(User updated) throws IOException {
        List<User> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) { all.set(i, updated); saveAll(all); return true; }
        }
        return false;
    }

    // DELETE
    public synchronized boolean delete(int id) throws IOException {
        List<User> all = getAll();
        boolean removed = all.removeIf(u -> u.getId() == id);
        if (removed) saveAll(all);
        return removed;
    }

    private void saveAll(List<User> users) throws IOException {
        List<String> lines = new ArrayList<>();
        for (User u : users) lines.add(u.toFileLine());
        file.writeLines(lines);
    }
}
