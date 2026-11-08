package org.com.recipes.cuisine.dto;

import lombok.Builder;
import org.com.recipes.recipe.dto.CollectionRecipeDto;

import java.util.Collection;

@Builder
public record CuisineDto(String id, String continent, String name, boolean spicy, Collection<CollectionRecipeDto> recipes) {
}
