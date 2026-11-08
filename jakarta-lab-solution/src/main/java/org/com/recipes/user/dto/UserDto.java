package org.com.recipes.user.dto;

import lombok.Builder;
import org.com.recipes.recipe.dto.CollectionRecipeDto;

import java.time.LocalDate;
import java.util.Collection;

@Builder
public record UserDto(String id, String username, String role, LocalDate registrationDate,
                       Collection<CollectionRecipeDto> recipes) {
}
