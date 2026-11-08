package org.com.recipes.recipe.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NoArgsConstructor;
import org.com.recipes.core.collection.UICollectionRepresentation;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.entity.id.RecipeId;
import org.com.recipes.recipe.mapper.RecipeEntityMapper;
import org.com.recipes.recipe.service.RecipeService;

import java.io.Serializable;

@Named
@ViewScoped
@NoArgsConstructor
public class RecipeListView implements Serializable {
    private RecipeService recipeService;
    private RecipeEntityMapper recipeEntityMapper;

    private UICollectionRepresentation<RecipeDto> recipes;

    @Inject
    public RecipeListView(RecipeService recipeService, RecipeEntityMapper recipeEntityMapper) {
        this.recipeService = recipeService;
        this.recipeEntityMapper = recipeEntityMapper;
    }

    public void init() {
        recipes = recipeEntityMapper.toUICollectionDtos(recipeService.getRecipes().items());
    }

    public UICollectionRepresentation<RecipeDto> getRecipes() {
        return recipes;
    }

    public void deleteRecipe(String id) {
        recipeService.deleteRecipe(RecipeId.of(id));
        init();
    }

    public String viewRecipe(String id) {
        return "/recipes/recipe-details-view?faces-redirect=true&id=" + id;
    }
}
