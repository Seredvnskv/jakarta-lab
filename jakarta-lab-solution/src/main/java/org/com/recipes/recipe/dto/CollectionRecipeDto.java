package org.com.recipes.recipe.dto;

import lombok.Builder;

@Builder
public record CollectionRecipeDto(String id, String title, String author, int servings, int calories, boolean vegetarian) {
}
