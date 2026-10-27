package org.com.recipes.recipe.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.mapper.RecipeMapper;
import org.com.recipes.recipe.service.RecipeService;

import java.io.Serializable;
import java.util.UUID;

@Named
@ViewScoped
@Getter
public class RecipeView implements Serializable {
    private final RecipeService recipeService;

    @Setter
    private String recipeId;
    private RecipeDto recipe;
    private String cuisineId;

    @Inject
    public RecipeView(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public void init() {
        UUID id = UUID.fromString(recipeId);
        Recipe entity = recipeService.findRecipeById(id);
        this.recipe = RecipeMapper.toDto(entity);
        this.cuisineId = entity.getCuisine().getId().toString();
    }

    public String backToCuisine() {
        return "/cuisines/cuisines_view?faces-redirect=true&cuisineId=" + cuisineId;
    }
}
