package org.com.recipes.recipe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.repository.RecipeRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RecipeService {

    private RecipeRepository recipeRepository;

    protected RecipeService() {}

    @Inject
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe findRecipeById(UUID id) throws NotFoundException {
        return recipeRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Recipe> findRecipesByCuisine(UUID cuisineId) {
        return recipeRepository.findByCuisineId(cuisineId);
    }

    public void createRecipe(Recipe recipe) throws InvalidDataException {
        recipeRepository.findById(recipe.getId()).ifPresent(_ -> {
            throw new InvalidDataException("Recipe already exists");
        });
        recipeRepository.save(recipe);
        System.out.println("Created recipe: %s (%s)".formatted(recipe.getTitle(), recipe.getId()));
    }

    public void deleteRecipe(UUID id) throws NotFoundException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(NotFoundException::new);
        recipeRepository.delete(recipe);
    }
}