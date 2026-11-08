package org.com.recipes.recipe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.cuisine.entity.Cuisine;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.cuisine.repository.CuisineRepository;
import org.com.recipes.recipe.dto.CreateRecipeDto;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.entity.Recipe;
import org.com.recipes.recipe.entity.id.RecipeId;
import org.com.recipes.recipe.mapper.RecipeEntityMapper;
import org.com.recipes.recipe.repository.RecipeRepository;
import org.com.recipes.user.entity.User;
import org.com.recipes.user.entity.id.UserId;
import org.com.recipes.user.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;

@ApplicationScoped
@NoArgsConstructor
public class RecipeService {
    private RecipeRepository recipeRepository;
    private RecipeEntityMapper recipeEntityMapper;
    private CuisineRepository cuisineRepository;
    private UserRepository userRepository;

    @Inject
    public RecipeService(RecipeRepository recipeRepository, RecipeEntityMapper recipeEntityMapper,
                          CuisineRepository cuisineRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeEntityMapper = recipeEntityMapper;
        this.cuisineRepository = cuisineRepository;
        this.userRepository = userRepository;
    }

    public CollectionRepresentation<RecipeDto> getRecipes() {
        Collection<Recipe> recipes = recipeRepository.findAll();
        return CollectionRepresentation.of(recipeEntityMapper.toDtos(recipes));
    }

    public RecipeDto getRecipe(RecipeId id) {
        return recipeRepository.findById(id).map(recipeEntityMapper::toDto).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public RecipeDto createRecipe(CreateRecipeDto dto) {
        Recipe recipe = recipeEntityMapper.toEntity(dto);
        resolveCuisine(dto.cuisine()).addRecipe(recipe);
        resolveAuthor(dto.author()).addRecipe(recipe);
        Recipe saved = recipeRepository.save(recipe);
        return recipeEntityMapper.toDto(saved);
    }

    @Transactional
    public RecipeDto updateRecipe(RecipeId id, CreateRecipeDto dto) {
        Recipe existing = recipeRepository.findById(id).orElseThrow(NotFoundException::new);
        existing.setTitle(dto.title());
        existing.setDescription(dto.description());
        existing.setIngredients(new HashSet<>(dto.ingredients()));
        existing.setServings(dto.servings());
        existing.setCalories(dto.calories());
        existing.setVegetarian(dto.vegetarian());

        existing.getCuisine().removeRecipe(existing);
        resolveCuisine(dto.cuisine()).addRecipe(existing);

        existing.getAuthor().removeRecipe(existing);
        resolveAuthor(dto.author()).addRecipe(existing);

        Recipe saved = recipeRepository.save(existing);
        return recipeEntityMapper.toDto(saved);
    }

    @Transactional
    public void deleteRecipe(RecipeId id) {
        Recipe existing = recipeRepository.findById(id).orElseThrow(NotFoundException::new);
        existing.getCuisine().removeRecipe(existing);
        existing.getAuthor().removeRecipe(existing);
        recipeRepository.delete(existing);
    }

    private Cuisine resolveCuisine(String cuisineId) {
        return cuisineRepository.findById(CuisineId.of(cuisineId)).orElseThrow(NotFoundException::new);
    }

    private User resolveAuthor(String userId) {
        return userRepository.findById(UserId.of(userId)).orElseThrow(NotFoundException::new);
    }
}
