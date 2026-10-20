package org.com.recipes.recipe.dto;

import java.util.List;

public record RecipeListDto(String cuisine, String title, List<String> ingredients, String author) {
}
