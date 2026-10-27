package org.com.recipes.cuisine.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.mapper.CuisineMapper;
import org.com.recipes.cuisine.service.CuisineService;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.service.RecipeService;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
@Getter
@Setter
public class CuisineView implements Serializable {

    private final CuisineService cuisineService;
    private final RecipeService recipeService;

    private String cuisineId;
    private CuisineDto cuisine;
    private List<Recipe> recipes;

    @Inject
    public CuisineView(CuisineService cuisineService, RecipeService recipeService) {
        this.cuisineService = cuisineService;
        this.recipeService = recipeService;
    }

    public void init() {
        UUID id = UUID.fromString(cuisineId);
        cuisine = CuisineMapper.toDto(cuisineService.findCuisineById(id));
        recipes = recipeService.findRecipesByCuisine(id);
    }

    public String viewRecipe(Recipe recipe) {
        return "/recipes/recipes_view?faces-redirect=true&recipeId=" + recipe.getId();
    }

    public String editRecipe(Recipe recipe) {
        return "/recipes/recipes_form?faces-redirect=true&cuisineId=" + cuisineId + "&recipeId=" + recipe.getId();
    }

    public String addRecipe() {
        return "/recipes/recipes_form?faces-redirect=true&cuisineId=" + cuisineId;
    }

    public String deleteRecipe(Recipe recipe) {
        recipeService.deleteRecipe(recipe.getId());
        return "cuisines_view?faces-redirect=true&cuisineId=" + cuisineId;
    }
}
