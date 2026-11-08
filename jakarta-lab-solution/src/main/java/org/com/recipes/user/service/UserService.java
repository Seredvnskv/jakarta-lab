package org.com.recipes.user.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.user.dto.UserDto;
import org.com.recipes.user.entity.Role;
import org.com.recipes.user.entity.User;
import org.com.recipes.user.entity.id.UserId;
import org.com.recipes.user.mapper.UserEntityMapper;
import org.com.recipes.user.repository.UserRepository;

import java.util.Collection;

@ApplicationScoped
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserEntityMapper userEntityMapper;

    @Inject
    public UserService(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public CollectionRepresentation<UserDto> getUsers() {
        Collection<User> users = userRepository.findAll();
        return CollectionRepresentation.of(userEntityMapper.toDtos(users));
    }

    public UserDto getUser(UserId id) {
        return userRepository.findById(id).map(userEntityMapper::toDto).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UserDto createUser(UserDto dto) {
        User user = userEntityMapper.toEntity(dto);
        User saved = userRepository.save(user);
        return userEntityMapper.toDto(saved);
    }

    @Transactional
    public UserDto updateUser(UserId id, UserDto dto) {
        User existing = userRepository.findById(id).orElseThrow(NotFoundException::new);
        existing.setUsername(dto.username());
        existing.setRole(Role.valueOf(dto.role()));
        User saved = userRepository.save(existing);
        return userEntityMapper.toDto(saved);
    }

    @Transactional
    public void deleteUser(UserId id) {
        User existing = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userRepository.delete(existing);
    }
}
