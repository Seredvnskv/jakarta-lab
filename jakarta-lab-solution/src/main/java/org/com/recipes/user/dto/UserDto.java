package org.com.recipes.user.dto;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.com.recipes.recipe.dto.RecipeListDto;

import java.time.LocalDate;
import java.util.List;

@JsonbPropertyOrder({"id", "username", "role", "registrationDate", "recipes"})
public record UserDto(String id, String username, String role, LocalDate registrationDate, List<RecipeListDto> recipes) {
}
