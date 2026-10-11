package org.com.recipes.recipe.dto;

import java.time.LocalDate;

public record RecipeDto(String title, String description, int servings, double caloriesPerServing,
                        boolean vegetarian, LocalDate creationDate, String cuisine, String author) {
}
