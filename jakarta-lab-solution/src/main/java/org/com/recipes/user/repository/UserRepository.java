package org.com.recipes.user.repository;

import lombok.RequiredArgsConstructor;
import org.com.recipes.core.data.DataStore;
import org.com.recipes.core.exception.AlreadyExistsException;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.core.repository.Repository;
import org.com.recipes.user.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserRepository implements Repository<User, UUID> {

    private final DataStore dataStore;
    private final Path avatarDirectory;

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

    public byte[] getUserAvatar(UUID id) {
        dataStore.findById(id).orElseThrow(NotFoundException::new);
        try {
            return Files.readAllBytes(avatarPath(id));
        } catch (NoSuchFileException e) {
            throw new NotFoundException();
        } catch (IOException e) {
            throw new InvalidDataException(e);
        }
    }

    public void createUserAvatar(UUID id, byte[] avatar) {
        User user = dataStore.findById(id).orElseThrow(NotFoundException::new);
        Path file = avatarDirectory.resolve(user.getId() + ".png");
        if (Files.exists(file)) {
            throw new AlreadyExistsException("Avatar for user %s already exists".formatted(user.getUsername()));
        }
        try {
            Files.createDirectories(avatarDirectory);
            Files.write(file, avatar);
            user.setAvatar(avatar);
            dataStore.save(user);
        } catch (IOException e) {
            throw new InvalidDataException(e);
        }
    }

    public void updateUserAvatar(UUID id, byte[] avatar) {
        User user = dataStore.findById(id).orElseThrow(NotFoundException::new);
        Path file = avatarPath(id);
        if (!Files.exists(file)) {
            throw new NotFoundException();
        }
        try {
            Files.write(file, avatar);
            user.setAvatar(avatar);
            user.setAvatar(avatar);
            dataStore.save(user);
        } catch (IOException e) {
            throw new InvalidDataException(e);
        }
    }

    public void deleteUserAvatar(UUID id) {
        User user = dataStore.findById(id).orElseThrow(NotFoundException::new);
        try {
            Files.delete(avatarPath(id));
            user.setAvatar(null);
            dataStore.save(user);
        }
        catch (NoSuchFileException esf) {
            throw new NotFoundException();
        }
        catch (IOException e) {
            throw new InvalidDataException(e);
        }
    }

    private Path avatarPath(UUID id) {
        return avatarDirectory.resolve(id + ".png");
    }
}
