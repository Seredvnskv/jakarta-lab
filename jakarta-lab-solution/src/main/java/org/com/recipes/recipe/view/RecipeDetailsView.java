package org.com.recipes.recipe.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.entity.id.RecipeId;
import org.com.recipes.recipe.service.RecipeService;

import java.io.Serializable;

@Named
@ViewScoped
@NoArgsConstructor
public class RecipeDetailsView implements Serializable {
    private RecipeService recipeService;

    @Inject
    public RecipeDetailsView(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Getter
    @Setter
    private String id;

    @Getter
    private RecipeDto recipe;

    public void init() {
        recipe = recipeService.getRecipe(RecipeId.of(id));
    }

    public String delete() {
        recipeService.deleteRecipe(RecipeId.of(id));
        return "/recipes/recipe-list-view?faces-redirect=true";
    }

    public String back() {
        return "/recipes/recipe-list-view?faces-redirect=true";
    }
}
