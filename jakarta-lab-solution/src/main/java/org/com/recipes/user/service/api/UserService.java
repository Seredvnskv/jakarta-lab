package org.com.recipes.user.service.api;

import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> findAllUsers();
    User findUserById(UUID id) throws NotFoundException;
    void createUser(User user) throws InvalidDataException;
    void deleteUser(UUID id) throws NotFoundException;
    byte[] getUserAvatar(UUID id) throws NotFoundException;
    void createUserAvatar(UUID id, byte[] avatar) throws NotFoundException, InvalidDataException;
    void updateUserAvatar(UUID id, byte[] avatar) throws NotFoundException, InvalidDataException;
    void deleteUserAvatar(UUID id) throws NotFoundException, InvalidDataException;
}
