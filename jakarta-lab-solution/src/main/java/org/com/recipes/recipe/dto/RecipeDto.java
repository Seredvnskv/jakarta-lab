package org.com.recipes.recipe.dto;

import lombok.Builder;
import org.com.recipes.recipe.entity.Ingredient;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record RecipeDto(String id, String title, String description, String cuisine, String author, Set<Ingredient> ingredients,
                        int servings, int calories, boolean vegetarian, LocalDateTime createdAt) {
}
