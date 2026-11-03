package org.com.recipes.recipe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.repository.RecipeRepository;

import java.util.Collection;
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

    public Recipe findRecipeById(UUID id) {
        return recipeRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Recipe> findRecipesByCuisine(UUID cuisineId) {
        return recipeRepository.findByCuisineId(cuisineId);
    }

    public Recipe createRecipe(Recipe recipe) {
        recipeRepository.findById(recipe.getId()).ifPresent(_ -> {
            throw new InvalidDataException("Recipe already exists");
        });
        Recipe saved = recipeRepository.save(recipe);
        System.out.println("Created recipe: %s (%s)".formatted(recipe.getTitle(), recipe.getId()));
        return saved;
    }

    public void createAllRecipes(Collection<Recipe> recipes) {
        recipeRepository.saveAll(recipes);
    }

    public Recipe updateRecipe(Recipe recipe) {
        recipeRepository.findById(recipe.getId()).orElseThrow(NotFoundException::new);
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(UUID id) {
        recipeRepository.deleteRecipe(id);
    }

    public void deleteAllRecipes() {
        recipeRepository.deleteAll();
    }
}
