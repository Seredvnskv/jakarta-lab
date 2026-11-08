package org.com.recipes.user.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.com.recipes.core.mapper.EntityMapper;
import org.com.recipes.recipe.mapper.RecipeEntityMapper;
import org.com.recipes.user.dto.UserDto;
import org.com.recipes.user.entity.Role;
import org.com.recipes.user.entity.User;
import org.com.recipes.user.entity.id.UserId;

import java.util.Collection;

@ApplicationScoped
@NoArgsConstructor
public class UserEntityMapper implements EntityMapper<User, UserDto> {

    private RecipeEntityMapper recipeEntityMapper;

    @Inject
    public UserEntityMapper(RecipeEntityMapper recipeEntityMapper) {
        this.recipeEntityMapper = recipeEntityMapper;
    }

    @Override
    public Collection<UserDto> toDtos(Collection<User> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    @Override
    public UserDto toDto(User entity) {
        return UserDto.builder()
                .id(entity.getId().id().toString())
                .username(entity.getUsername())
                .role(entity.getRole().toString())
                .registrationDate(entity.getRegistrationDate())
                .recipes(recipeEntityMapper.toCollectionDtos(entity.getRecipes()))
                .build();
    }

    @Override
    public User toEntity(UserDto dto) {
        return User.builder()
                .id(UserId.of(dto.id()))
                .username(dto.username())
                .role(Role.valueOf(dto.role()))
                .registrationDate(dto.registrationDate())
                .build();
    }
}
