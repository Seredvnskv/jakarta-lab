package org.com.recipes.recipe.dto;

import org.com.recipes.recipe.entity.Ingredient;

import java.util.Set;

public record CreateRecipeDto(String title, String description, String cuisine, String author, Set<Ingredient> ingredients,
                               int servings, int calories, boolean vegetarian) {
}
