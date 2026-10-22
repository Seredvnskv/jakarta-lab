package org.com.recipes.user.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.com.recipes.core.config.AvatarConfig;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.core.util.ImageTypeDetector;
import org.com.recipes.user.User;
import org.com.recipes.user.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private AvatarConfig avatarConfig;

    @Inject
    public UserService(UserRepository userRepository, AvatarConfig avatarConfig) {
        this.userRepository = userRepository;
        this.avatarConfig = avatarConfig;
    }

    public List<User> provideUsers() {
        return userRepository.findAll();
    }

    public User provideUser(UUID id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void createUser(User user) {
        if (user.getAvatar() != null) {
            try {
                Files.write(resolvePath(user.getId(), user.getAvatar()), user.getAvatar());
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        userRepository.save(user);
        System.out.println("Created user %s (%s) : [%s]".formatted(user.getUsername(), user.getId(), LocalDateTime.now()));
    }

    public void createUser(User user, InputStream avatar) {
        if (avatar != null) {
            try (InputStream in = avatar) {
                byte[] bytes = in.readAllBytes();
                if (bytes.length == 0) {
                    throw new InvalidDataException("Invalid avatar");
                }
                Files.write(resolvePath(user.getId(), bytes), bytes);
                user.setAvatar(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        User user = provideUser(id);
        deleteAvatar(id);
        userRepository.delete(user);
    }

    public byte[] provideAvatar(UUID id) {
        User user = provideUser(id);
        byte[] avatar = userRepository.findAvatar(user);
        if (avatar == null) {
            throw new NotFoundException();
        }
        return avatar;
    }

    public void updateAvatar(UUID id, InputStream avatar) {
        try (InputStream in = avatar) {
            User user = provideUser(id);
            byte[] bytes = in.readAllBytes();
            if (bytes.length == 0) {
                throw new InvalidDataException("Invalid avatar");
            }
            Files.write(resolvePath(id, bytes), bytes);
            userRepository.updateAvatar(user, bytes);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAvatar(UUID id) {
        try {
            User user = provideUser(id);
            Files.deleteIfExists(resolvePath(id, user.getAvatar()));
            userRepository.deleteAvatar(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path resolvePath(UUID id, byte[] avatar) {
        String imageType = ImageTypeDetector.detectImageType(avatar).split("/")[1];
        return avatarConfig.getAvatarPath().resolve(id.toString() + "." + imageType);
    }
}
