package org.com.recipes.user.dto;

import org.com.recipes.recipe.dto.RecipeListDto;

import java.time.LocalDate;
import java.util.List;

public record UserDto(String id, String username, String role, LocalDate registrationDate, List<RecipeListDto> recipes) {
}
