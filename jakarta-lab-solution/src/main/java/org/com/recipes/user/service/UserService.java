package org.com.recipes.user.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.core.exception.AccessForbiddenException;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.core.exception.UnauthorizedAccessException;
import org.com.recipes.recipe.entity.Recipe;
import org.com.recipes.recipe.repository.RecipeRepository;
import org.com.recipes.user.dto.CreateUserDto;
import org.com.recipes.user.dto.RegisterUserDto;
import org.com.recipes.user.dto.UserDto;
import org.com.recipes.user.entity.Role;
import org.com.recipes.user.entity.User;
import org.com.recipes.user.entity.id.UserId;
import org.com.recipes.user.mapper.UserEntityMapper;
import org.com.recipes.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Users: registration is open to everybody, USER may read only own account, everything else requires ADMIN.
 */
@ApplicationScoped
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private RecipeRepository recipeRepository;
    private UserEntityMapper userEntityMapper;
    private Pbkdf2PasswordHash passwordHash;
    private SecurityContext securityContext;

    @Inject
    public UserService(UserRepository userRepository, RecipeRepository recipeRepository, UserEntityMapper userEntityMapper, Pbkdf2PasswordHash passwordHash, SecurityContext securityContext) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.userEntityMapper = userEntityMapper;
        this.passwordHash = passwordHash;
        this.securityContext = securityContext;
    }

    public CollectionRepresentation<UserDto> getUsers() {
        requireAdmin();
        Collection<User> users = userRepository.findAll();
        return CollectionRepresentation.of(userEntityMapper.toDtos(users));
    }

    public UserDto getUser(UserId id) {
        requireAuthenticated();
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        boolean isOwnAccount = user.getUsername().equals(securityContext.getCallerPrincipal().getName());
        if (!isAdmin() && !isOwnAccount) {
            throw new AccessForbiddenException("Users may only read their own account");
        }
        return userEntityMapper.toDto(user);
    }

    /**
     * Creates a user with explicitly given id and role - administrators only.
     */
    @Transactional
    public UserDto createUser(CreateUserDto dto) {
        requireAdmin();
        return doCreateUser(dto);
    }

    /**
     * Public self-registration: always creates a regular user with a generated id and current date.
     */
    @Transactional
    public UserDto registerUser(RegisterUserDto dto) {
        if (dto == null) {
            throw new InvalidDataException("Request body is required");
        }
        return doCreateUser(CreateUserDto.builder()
                .id(UserId.random().id().toString())
                .username(dto.username() != null ? dto.username().trim() : null)
                .password(dto.password())
                .role(Role.USER.name())
                .registrationDate(LocalDate.now())
                .build());
    }

    @Transactional
    public UserDto updateUser(UserId id, UserDto dto) {
        requireAdmin();
        User existing = userRepository.findById(id).orElseThrow(NotFoundException::new);
        if (dto.username() != null && !dto.username().equals(existing.getUsername())) {
            if (dto.username().isBlank()) {
                throw new InvalidDataException("Username must not be blank");
            }
            if (userRepository.findByUsername(dto.username()).isPresent()) {
                throw new InvalidDataException("Username " + dto.username() + " is already taken");
            }
            existing.setUsername(dto.username());
        }
        if (dto.role() != null) {
            existing.setRole(Role.valueOf(dto.role()));
        }
        User saved = userRepository.save(existing);
        return userEntityMapper.toDto(saved);
    }

    @Transactional
    public void deleteUser(UserId id) {
        requireAdmin();
        User existing = userRepository.findById(id).orElseThrow(NotFoundException::new);
        for (Recipe recipe : new ArrayList<>(existing.getRecipes())) {
            existing.removeRecipe(recipe);
            recipeRepository.delete(recipe);
        }
        userRepository.delete(existing);
    }

    /**
     * Stores the user with a hashed password (plain text password expected in the dto), without any access check.
     */
    private UserDto doCreateUser(CreateUserDto dto) {
        validateCredentials(dto.username(), dto.password());
        if (dto.id() != null && userRepository.findById(UserId.of(dto.id())).isPresent()) {
            throw new InvalidDataException("User with id " + dto.id() + " already exists");
        }
        User user = userEntityMapper.toEntity(dto);
        if (user.getId() == null) {
            user.setId(UserId.random());
        }
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDate.now());
        }
        user.setPassword(passwordHash.generate(dto.password().toCharArray()));
        User saved = userRepository.save(user);
        return userEntityMapper.toDto(saved);
    }

    private boolean isAdmin() {
        return securityContext.isCallerInRole(Role.ADMIN.name());
    }

    private void requireAuthenticated() {
        if (securityContext.getCallerPrincipal() == null) {
            throw new UnauthorizedAccessException("Authentication required");
        }
    }

    private void requireAdmin() {
        requireAuthenticated();
        if (!isAdmin()) {
            throw new AccessForbiddenException("Administrator role required");
        }
    }

    private void validateCredentials(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new InvalidDataException("Username must not be blank");
        }
        if (password == null || password.isBlank()) {
            throw new InvalidDataException("Password must not be blank");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new InvalidDataException("Username " + username + " is already taken");
        }
    }
}
