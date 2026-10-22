package org.com.recipes.user;

public enum Role {
    USER,
    ADMIN;

    public static Role fromString(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
