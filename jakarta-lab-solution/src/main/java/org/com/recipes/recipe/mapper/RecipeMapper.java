package org.com.recipes.recipe.mapper;

import lombok.experimental.UtilityClass;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.dto.RecipeListDto;

@UtilityClass
public final class RecipeMapper {
    public static RecipeListDto toRecipeListDto(Recipe recipe) {
        return new RecipeListDto(recipe.getCuisine().getName(), recipe.getTitle(), recipe.getIngredients(), recipe.getAuthor().getUsername());
    }
}
