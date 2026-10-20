package org.com.recipes.user.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.com.recipes.core.exception.AlreadyExistsException;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.user.User;
import org.com.recipes.user.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    private UserRepository userRepository;

    protected UserService() {}

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(UUID id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void createUser(User user) throws InvalidDataException {
        userRepository.findById(user.getId()).ifPresent(_ -> {
            throw new InvalidDataException("User already exists");
        });
        userRepository.createUser(user);
        System.out.println("Created user: %s (%s)".formatted(user.getUsername(), user.getId()));
    }

    public void deleteUser(UUID id) throws NotFoundException {
        userRepository.deleteUser(id);
    }

    public byte[] getUserAvatar(UUID id) throws NotFoundException {
        return userRepository.provideUserAvatar(id);
    }

    public void createUserAvatar(UUID id, byte[] avatar) throws AlreadyExistsException {
        userRepository.createUserAvatar(id, avatar);
    }

    public void updateUserAvatar(UUID id, byte[] avatar) throws NotFoundException, InvalidDataException {
        userRepository.updateUserAvatar(id, avatar);
    }

    public void deleteUserAvatar(UUID id) throws NotFoundException, InvalidDataException {
        userRepository.deleteUserAvatar(id);
    }
}
