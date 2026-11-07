package com.quickbite.model;

/** Encapsulation: private fields. Subclasses decide role-specific behaviour (polymorphism). */
public abstract class User implements java.io.Serializable {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private String phone;
    private String address;

    protected User(int id, String username, String passwordHash, String email, String phone, String address) {
        this.id = id; this.username = username; this.passwordHash = passwordHash;
        this.email = email; this.phone = phone; this.address = address;
    }

    public abstract String getRole();
    public abstract boolean isAdmin();
    public abstract String getGreeting();

    /** Returns the first letter of the username in uppercase, handling Unicode codepoints safely. */
    public String getAvatarInitial() {
        if (username == null || username.trim().isEmpty()) return "U";
        String clean = username.trim();
        int codePoint = clean.codePointAt(0);
        return new String(Character.toChars(codePoint)).toUpperCase(java.util.Locale.ROOT);
    }

    /** Returns a deterministic, attractive background color from a curated palette. */
    public String getAvatarColor() {
        if (username == null || username.trim().isEmpty()) return "#ff8a1f";
        String[] palette = {
            "#e5372b", "#ff8a1f", "#d97706", "#059669", "#0d9488",
            "#0284c7", "#2563eb", "#4f46e5", "#7c3aed", "#c026d3",
            "#db2777", "#e11d48", "#b45309", "#047857", "#1d4ed8"
        };
        int hash = 0;
        for (char c : username.trim().toCharArray()) hash = 31 * hash + c;
        int idx = Math.abs(hash) % palette.length;
        return palette[idx];
    }

    /** id|username|passwordHash|email|phone|address|role */
    public String toFileLine() {
        return id + "|" + username + "|" + passwordHash + "|" + email + "|" + phone + "|" + address + "|" + getRole();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
