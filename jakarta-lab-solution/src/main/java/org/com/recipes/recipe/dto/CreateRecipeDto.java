package org.com.recipes.recipe.dto;

import java.util.List;

public record CreateRecipeDto(String authorId, List<String> ingredients, String title, String description, int servings, double caloriesPerServing, boolean vegetarian) {
}
