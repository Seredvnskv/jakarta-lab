package org.com.recipes.user.mapper;

import lombok.experimental.UtilityClass;
import org.com.recipes.recipe.mapper.RecipeMapper;
import org.com.recipes.user.Role;
import org.com.recipes.user.User;
import org.com.recipes.user.dto.CreateUserDto;
import org.com.recipes.user.dto.UserDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@UtilityClass
public final class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId().toString(), user.getUsername(), user.getRole().toString(),
                user.getRegistrationDate(), user.getRecipes().stream().map(RecipeMapper::toRecipeListDto).toList());
    }

    public static List<UserDto> toUserDto(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).toList();
    }

    public static User toEntity(CreateUserDto dto) {
        return User.builder()
                .id(dto.id() == null || dto.id().isBlank() ? UUID.randomUUID() : UUID.fromString(dto.id()))
                .username(dto.username())
                .role(Role.fromString(dto.role()))
                .registrationDate(LocalDate.now())
                .build();
    }
}
