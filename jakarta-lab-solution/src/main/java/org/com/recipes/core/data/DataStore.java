package org.com.recipes.core.data;


import jakarta.annotation.PostConstruct;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.user.User;

import java.io.InputStream;
import java.util.*;

public class DataStore {
    private final Map<UUID, User> users = new HashMap<>();

    public void init() {
        try (InputStream in = getClass().getResourceAsStream("/users.json")) {
            Jsonb jsonb = JsonbBuilder.create();

            if (in == null) {
                throw new InvalidDataException("Users file not found");
            }

            User[] users = jsonb.fromJson(in, User[].class);
            Arrays.stream(users).forEach(this::save);
            System.out.println("Successfully loaded " + users.length + " users");
            jsonb.close();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Could not load users.json", e);
        }
    }

    public synchronized Optional<User> findById(UUID id) {
        return Optional.ofNullable(copy(users.get(id)));
    }

    public synchronized List<User> findAll() {
        return users.values().stream().map(this::copy).toList();
    }

    public synchronized User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public synchronized void delete(UUID id) {
        users.remove(id);
    }

    private User copy(User user) {
        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .registrationDate(user.getRegistrationDate())
                .recipes(user.getRecipes())
                .avatar(user.getAvatar())
                .build();
    }
}
