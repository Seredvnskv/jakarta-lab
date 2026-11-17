package org.com.recipes.user.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.user.entity.id.UserId;
import org.com.recipes.user.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;

@ApplicationScoped
@NoArgsConstructor
public class AvatarService {
    private UserRepository userRepository;
    private Path avatarsDirectory;

    private final static byte[] JPEG = new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private final static byte[] PNG = new byte[] {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};

    @Inject
    public AvatarService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Properties properties = new Properties();
        try (var input = AvatarService.class.getResourceAsStream("/application.properties")) {
            if (input == null) {
                throw new IllegalStateException("application.properties not found");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read application.properties", e);
        }
        String configuredPath = properties.getProperty("avatars.directory");
        if (configuredPath == null || configuredPath.isBlank()) {
            throw new IllegalStateException("avatars.directory property is empty");
        }

        this.avatarsDirectory = Path.of(configuredPath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(avatarsDirectory);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create avatars directory", e);
        }
    }

    public byte[] findAvatar(UserId id) {
        userRepository.findById(id).orElseThrow(NotFoundException::new);
        try {
            return Files.readAllBytes(avatarsDirectory.resolve(id.id().toString()));
        }
        catch (NoSuchFileException e) {
            throw new NotFoundException();
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not read avatars directory", e);
        }
    }

    public void createAvatar(UserId id, byte[] image) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            saveImage(id, image);
        }, () -> {
            throw new NotFoundException();
        });
    }

    private void saveImage(UserId id, byte[] image) {
        checkImageFormat(image);
        Path target = avatarsDirectory.resolve(id.id().toString());
        try {
            Files.write(target, image);
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not write avatar file", e);
        }
    }

    public void updateAvatar(UserId id, byte[] image) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            if (!Files.isRegularFile(avatarsDirectory.resolve(id.id().toString()))) {
                throw new NotFoundException();
            }
            saveImage(id, image);
        }, () -> {
            throw new NotFoundException();
        });
    }

    public void deleteAvatar(UserId id) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            Path target = avatarsDirectory.resolve(id.id().toString());
            try {
                Files.deleteIfExists(target);
            } catch (IOException e) {
                throw new IllegalStateException("Could not delete avatar file", e);
            }
        }, () -> {
            throw new NotFoundException();
        });
    }

    public void deleteAllAvatars() {
        try (var files = Files.newDirectoryStream(avatarsDirectory)) {
            for (Path file : files) {
                if (Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
                    Files.deleteIfExists(file);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not delete avatar files", e);
        }
    }

    private void checkImageFormat(byte[] image) {
        getImageContentType(image);
    }

    public String getImageContentType(byte[] image) {
        if (checkSignature(image, JPEG)) {
            return "image/jpeg";
        }
        if (checkSignature(image, PNG)) {
            return "image/png";
        }
        throw new InvalidDataException("Invalid image format");
    }

    private boolean checkSignature(byte[] image, byte[] signature) {
        return image != null && image.length >= signature.length
                && Arrays.equals(image, 0, signature.length, signature, 0, signature.length);
    }
}
