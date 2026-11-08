package org.com.recipes.recipe.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.com.recipes.core.collection.UICollectionRepresentation;
import org.com.recipes.core.mapper.EntityMapper;
import org.com.recipes.recipe.dto.CollectionRecipeDto;
import org.com.recipes.recipe.dto.CreateRecipeDto;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.entity.Recipe;
import org.com.recipes.recipe.entity.id.RecipeId;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeEntityMapper implements EntityMapper<Recipe, RecipeDto> {
    @Override
    public Collection<RecipeDto> toDtos(Collection<Recipe> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    @Override
    public RecipeDto toDto(Recipe entity) {
        return RecipeDto.builder()
                .id(entity.getId().id().toString())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .cuisine(entity.getCuisine().getId().id().toString())
                .author(entity.getAuthor().getId().id().toString())
                .ingredients(entity.getIngredients())
                .servings(entity.getServings())
                .calories(entity.getCalories())
                .vegetarian(entity.isVegetarian())
                .createdAt(entity.getCreationTime())
                .build();
    }

    @Override
    public Recipe toEntity(RecipeDto dto) {
        return Recipe.builder()
                .id(RecipeId.of(dto.id()))
                .title(dto.title())
                .description(dto.description())
                .ingredients(new HashSet<>(dto.ingredients()))
                .servings(dto.servings())
                .calories(dto.calories())
                .vegetarian(dto.vegetarian())
                .creationTime(dto.createdAt())
                .build();
    }

    public Recipe toEntity(CreateRecipeDto dto) {
        return Recipe.builder()
                .id(RecipeId.random())
                .title(dto.title())
                .description(dto.description())
                .ingredients(new HashSet<>(dto.ingredients()))
                .servings(dto.servings())
                .calories(dto.calories())
                .vegetarian(dto.vegetarian())
                .creationTime(LocalDateTime.now())
                .build();
    }

    public CollectionRecipeDto toCollectionDto(Recipe entity) {
        return CollectionRecipeDto.builder()
                .id(entity.getId().id().toString())
                .title(entity.getTitle())
                .author(entity.getAuthor().getUsername())
                .servings(entity.getServings())
                .calories(entity.getCalories())
                .vegetarian(entity.isVegetarian())
                .build();
    }

    public Collection<CollectionRecipeDto> toCollectionDtos(Collection<Recipe> entities) {
        return entities.stream().map(this::toCollectionDto).collect(Collectors.toCollection(HashSet::new));
    }

    public UICollectionRepresentation<RecipeDto> toUICollectionDtos(Collection<RecipeDto> dtos) {
        return new UICollectionRepresentation<>(dtos);
    }
}
