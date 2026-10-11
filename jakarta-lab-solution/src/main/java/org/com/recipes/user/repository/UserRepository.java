package org.com.recipes.user.repository;

import lombok.RequiredArgsConstructor;
import org.com.recipes.core.data.DataStore;
import org.com.recipes.core.repository.Repository;
import org.com.recipes.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserRepository implements Repository<User, UUID> {

    private final DataStore dataStore;

    @Override
    public Optional<User> findById(UUID id) {
        return dataStore.findById(id);
    }

    @Override
    public List<User> findAll() {
        return dataStore.findAll();
    }

    @Override
    public User save(User user) {
        return dataStore.save(user);
    }

    @Override
    public void delete(User user) {
        dataStore.delete(user.getId());
    }
}
