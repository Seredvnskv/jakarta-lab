package org.com.recipes.user.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.com.recipes.core.exception.AlreadyExistsException;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.core.repository.Repository;
import org.com.recipes.core.util.AvatarDirectoryProvider;
import org.com.recipes.core.util.ImageTypeDetector;
import org.com.recipes.user.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class UserRepository implements Repository<User, UUID> {

    private EntityManager em;
    private AvatarDirectoryProvider avatarDirectoryProvider;

    protected UserRepository() {}

    @Inject
    public UserRepository(EntityManager em, AvatarDirectoryProvider avatarDirectoryProvider) {
        this.em = em;
        this.avatarDirectoryProvider = avatarDirectoryProvider;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User save(User user) {
        em.merge(user);
        return user;
    }

    @Override
    public void delete(User user) {
        em.remove(user);
    }

    public User createUser(User user) {
        byte[] avatar = user.getAvatar();
        user.setAvatar(null);
        save(user);
        if (avatar != null) {
            createUserAvatar(user.getId(), avatar);
        }
        return user;
    }

    public void deleteUser(UUID id) {
        User user = provideUser(id);
        if (user.getAvatar() != null) {
            deleteUserAvatar(id);
        }
        em.remove(user);
    }

    public byte[] provideUserAvatar(UUID id) {
        User user = provideUser(id);
        if (user.getAvatar() == null) {
            throw new NotFoundException();
        }
        try {
            return Files.readAllBytes(avatarPath(user.getId(), user.getAvatar()));
        } catch (IOException e) {
            throw new InvalidDataException(e);
        }
    }

    private User provideUser(UUID id) {
        User user = em.find(User.class, id);
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    public void createUserAvatar(UUID id, byte[] avatar) {
        User user = provideUser(id);
        if (user.getAvatar() != null) {
            throw new AlreadyExistsException("Avatar for user %s already exists".formatted(user.getUsername()));
        }
        try {
            Files.createDirectories(avatarDirectoryProvider.getAvatarDirectory());
            Files.write(avatarPath(user.getId(), avatar), avatar);
        } catch (IOException e) {
            throw new InvalidDataException(e);
        }
        user.setAvatar(avatar);
    }

    public void updateUserAvatar(UUID id, byte[] avatar) {
        User user = provideUser(id);
        if (user.getAvatar() == null) {
            throw new NotFoundException();
        }
        Path oldPath = avatarPath(user.getId(), user.getAvatar());
        Path newPath = avatarPath(user.getId(), avatar);
        try {
            Files.write(newPath, avatar);
            if (!newPath.equals(oldPath)) {
                Files.deleteIfExists(oldPath);
            }
        } catch (IOException e) {
            throw new InvalidDataException(e);
        }
        user.setAvatar(avatar);
    }

    public void deleteUserAvatar(UUID id) {
        User user = provideUser(id);
        if (user.getAvatar() == null) {
            throw new NotFoundException();
        }
        try {
            Files.delete(avatarPath(user.getId(), user.getAvatar()));
        } catch (IOException e) {
            throw new InvalidDataException(e);
        }
        user.setAvatar(null);
    }

    private Path avatarPath(UUID userId, byte[] avatar) {
        String imageType = ImageTypeDetector.detectImageType(avatar).split("/")[1];
        return avatarDirectoryProvider.getAvatarDirectory().resolve(userId + "." + imageType);
    }
}
