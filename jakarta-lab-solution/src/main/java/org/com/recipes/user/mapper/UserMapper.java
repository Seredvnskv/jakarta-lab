package org.com.recipes.user.mapper;

import lombok.experimental.UtilityClass;
import org.com.recipes.recipe.mapper.RecipeMapper;
import org.com.recipes.user.User;
import org.com.recipes.user.dto.UserDto;

import java.util.List;

@UtilityClass
public final class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId().toString(), user.getUsername(), user.getRole().toString(), user.getRegistrationDate(), user.getRecipes().stream().map(RecipeMapper::toRecipeListDto).toList());
    }

    public static List<UserDto> toUserDto(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).toList();
    }
}
