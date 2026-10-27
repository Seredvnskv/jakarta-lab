package org.com.recipes.recipe.mapper;

import lombok.experimental.UtilityClass;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.dto.CreateRecipeDto;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.dto.RecipeListDto;
import org.com.recipes.user.User;

import java.time.LocalDate;
import java.util.UUID;

@UtilityClass
public final class RecipeMapper {
    public static RecipeListDto toRecipeListDto(Recipe recipe) {
        return new RecipeListDto(recipe.getCuisine().getName(), recipe.getTitle(), recipe.getIngredients(), authorUsername(recipe));
    }

    public static RecipeDto toRecipeDto(Recipe recipe) {
        return new RecipeDto(recipe.getTitle(), recipe.getIngredients(), recipe.getDescription(), recipe.getServings(),
                recipe.getCaloriesPerServing(), recipe.isVegetarian(), recipe.getCreationDate(),
                recipe.getCuisine().getName(), authorUsername(recipe));
    }

    private static String authorUsername(Recipe recipe) {
        return recipe.getAuthor() == null ? null : recipe.getAuthor().getUsername();
    }

    public static Recipe toEntity(CreateRecipeDto dto, Cuisine cuisine, User author) {
        return Recipe.builder()
                .id(UUID.randomUUID())
                .cuisine(cuisine)
                .author(author)
                .ingredients(dto.ingredients())
                .title(dto.title())
                .description(dto.description())
                .servings(dto.servings())
                .caloriesPerServing(dto.caloriesPerServing())
                .vegetarian(dto.vegetarian())
                .creationDate(LocalDate.now())
                .build();
    }
}
