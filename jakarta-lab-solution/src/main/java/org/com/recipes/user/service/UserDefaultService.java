package org.com.recipes.user.service;

import lombok.RequiredArgsConstructor;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.user.User;
import org.com.recipes.user.repository.UserRepository;
import org.com.recipes.user.service.api.UserService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UserDefaultService implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(UUID id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void createUser(User user) throws InvalidDataException {
        userRepository.findById(user.getId()).ifPresent(_ -> {
            throw new InvalidDataException("User already exists");
        });
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userRepository.delete(user);
    }
}
