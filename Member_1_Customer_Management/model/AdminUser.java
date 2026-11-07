package com.quickbite.model;

/** An administrator: can manage foods, orders and users. */
public class AdminUser extends User {
    public AdminUser(int id, String username, String passwordHash, String email, String phone, String address) {
        super(id, username, passwordHash, email, phone, address);
    }
    @Override public String getRole() { return "ADMIN"; }
    @Override public boolean isAdmin() { return true; }
    @Override public String getGreeting() { return "Admin " + getUsername(); }
}
