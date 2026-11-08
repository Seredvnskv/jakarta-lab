package org.com.recipes.cuisine.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.cuisine.service.CuisineService;
import org.com.recipes.recipe.entity.id.RecipeId;
import org.com.recipes.recipe.service.RecipeService;

import java.io.Serializable;

@Named
@ViewScoped
@NoArgsConstructor
public class CuisineDetailsView implements Serializable {
    private CuisineService cuisineService;
    private RecipeService recipeService;

    @Inject
    public CuisineDetailsView(CuisineService cuisineService, RecipeService recipeService) {
        this.cuisineService = cuisineService;
        this.recipeService = recipeService;
    }

    @Getter
    @Setter
    private String id;

    @Getter
    private CuisineDto cuisine;

    public void init() {
        load();
    }

    public String delete() {
        cuisineService.deleteCuisine(CuisineId.of(id));
        return "/cuisines/cuisine-list-view?faces-redirect=true";
    }

    public void deleteRecipe(String recipeId) {
        recipeService.deleteRecipe(RecipeId.of(recipeId));
        load();
    }

    public String back() {
        return "/cuisines/cuisine-list-view?faces-redirect=true";
    }

    private void load() {
        cuisine = cuisineService.getCuisine(CuisineId.of(id));
    }
}
