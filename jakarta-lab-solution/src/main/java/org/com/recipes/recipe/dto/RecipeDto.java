package org.com.recipes.recipe.dto;

import java.time.LocalDate;
import java.util.List;

public record RecipeDto(String title, List<String> ingredients, String description, int servings, double caloriesPerServing,
                        boolean vegetarian, LocalDate creationDate, String cuisine, String author) {
}
