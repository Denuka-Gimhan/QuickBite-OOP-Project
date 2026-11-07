package com.quickbite.model;

/** A normal customer. */
public class RegularUser extends User {
    public RegularUser(int id, String username, String passwordHash, String email, String phone, String address) {
        super(id, username, passwordHash, email, phone, address);
    }
    @Override public String getRole() { return "CUSTOMER"; }
    @Override public boolean isAdmin() { return false; }
    @Override public String getGreeting() { return "Hi, " + getUsername(); }
}
