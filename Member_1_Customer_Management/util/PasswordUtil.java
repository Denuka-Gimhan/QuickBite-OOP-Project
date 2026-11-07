package com.quickbite.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Passwords are never stored as plain text. We store  salt$SHA-256(salt + password).
 * A random salt per user means two users with the same password get different hashes.
 * Old unsalted hashes (64 hex characters, no '$') are still accepted so existing users.txt files keep working.
 */
public final class PasswordUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private PasswordUtil() {}

    public static String hash(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return HexFormat.of().formatHex(salt) + "$" + HexFormat.of().formatHex(sha256(salt, password));
    }

    public static boolean matches(String password, String stored) {
        if (password == null || stored == null) return false;
        int i = stored.indexOf('$');
        if (i < 0) {   // legacy unsalted hash
            return MessageDigest.isEqual(stored.getBytes(StandardCharsets.UTF_8),
                    HexFormat.of().formatHex(sha256(new byte[0], password)).getBytes(StandardCharsets.UTF_8));
        }
        try {
            byte[] salt = HexFormat.of().parseHex(stored.substring(0, i));
            byte[] expected = HexFormat.of().parseHex(stored.substring(i + 1));
            return MessageDigest.isEqual(expected, sha256(salt, password));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] sha256(byte[] salt, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
