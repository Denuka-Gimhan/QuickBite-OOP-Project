package com.quickbite.model;

public final class UserFactory {
    private UserFactory() {}

    public static User create(int id, String username, String hash, String email, String phone, String address, String role) {
        return "ADMIN".equals(role)
                ? new AdminUser(id, username, hash, email, phone, address)
                : new RegularUser(id, username, hash, email, phone, address);
    }
}
