package org.com.recipes.recipe.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.core.exception.AccessForbiddenException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.core.exception.UnauthorizedAccessException;
import org.com.recipes.cuisine.entity.Cuisine;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.cuisine.repository.CuisineRepository;
import org.com.recipes.recipe.dto.CreateRecipeDto;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.entity.Recipe;
import org.com.recipes.recipe.entity.id.RecipeId;
import org.com.recipes.recipe.mapper.RecipeEntityMapper;
import org.com.recipes.recipe.repository.RecipeRepository;
import org.com.recipes.user.entity.Role;
import org.com.recipes.user.entity.User;
import org.com.recipes.user.entity.id.UserId;
import org.com.recipes.user.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class RecipeService {
    private RecipeRepository recipeRepository;
    private RecipeEntityMapper recipeEntityMapper;
    private CuisineRepository cuisineRepository;
    private UserRepository userRepository;
    private SecurityContext securityContext;

    @Inject
    public RecipeService(RecipeRepository recipeRepository, RecipeEntityMapper recipeEntityMapper,
                         CuisineRepository cuisineRepository, UserRepository userRepository,
                         SecurityContext securityContext) {
        this.recipeRepository = recipeRepository;
        this.recipeEntityMapper = recipeEntityMapper;
        this.cuisineRepository = cuisineRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    public CollectionRepresentation<RecipeDto> getRecipes() {
        User caller = requireCaller();
        Collection<Recipe> recipes = isAdmin()
                ? recipeRepository.findAll()
                : recipeRepository.findAllByAuthor(caller);
        return CollectionRepresentation.of(recipeEntityMapper.toDtos(recipes));
    }

    public RecipeDto getRecipe(RecipeId id) {
        return findAccessible(id).map(recipeEntityMapper::toDto).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public RecipeDto createRecipe(CreateRecipeDto dto) {
        User caller = requireCaller();
        User owner = isAdmin() && dto.author() != null ? resolveAuthor(dto.author()) : caller;
        Cuisine cuisine = resolveCuisine(dto.cuisine());

        Recipe recipe = recipeEntityMapper.toEntity(dto);
        cuisine.addRecipe(recipe);
        owner.addRecipe(recipe);
        Recipe saved = recipeRepository.save(recipe);
        return recipeEntityMapper.toDto(saved);
    }

    @Transactional
    public RecipeDto updateRecipe(RecipeId id, CreateRecipeDto dto) {
        Recipe existing = findAccessible(id).orElseThrow(NotFoundException::new);
        existing.setTitle(dto.title());
        existing.setDescription(dto.description());
        existing.setIngredients(new HashSet<>(dto.ingredients()));
        existing.setServings(dto.servings());
        existing.setCalories(dto.calories());
        existing.setVegetarian(dto.vegetarian());

        existing.getCuisine().removeRecipe(existing);
        resolveCuisine(dto.cuisine()).addRecipe(existing);

        // only ADMIN may change the owner, for USER the author in the request is ignored
        if (isAdmin() && dto.author() != null) {
            existing.getAuthor().removeRecipe(existing);
            resolveAuthor(dto.author()).addRecipe(existing);
        }

        Recipe saved = recipeRepository.save(existing);
        return recipeEntityMapper.toDto(saved);
    }

    @Transactional
    public void deleteRecipe(RecipeId id) {
        Recipe existing = findAccessible(id).orElseThrow(NotFoundException::new);
        existing.getCuisine().removeRecipe(existing);
        existing.getAuthor().removeRecipe(existing);
        recipeRepository.delete(existing);
    }

    private Optional<Recipe> findAccessible(RecipeId id) {
        User caller = requireCaller();
        return isAdmin()
                ? recipeRepository.findById(id)
                : recipeRepository.findByIdAndAuthor(id, caller);
    }

    private boolean isAdmin() {
        return securityContext.isCallerInRole(Role.ADMIN.name());
    }

    private User requireCaller() {
        if (securityContext.getCallerPrincipal() == null) {
            throw new UnauthorizedAccessException("Authentication required");
        }
        if (!isAdmin() && !securityContext.isCallerInRole(Role.USER.name())) {
            throw new AccessForbiddenException("Role USER or ADMIN required");
        }
        String username = securityContext.getCallerPrincipal().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AccessForbiddenException("Caller " + username + " is not a registered user"));
    }

    private Cuisine resolveCuisine(String cuisineId) {
        return cuisineRepository.findById(CuisineId.of(cuisineId)).orElseThrow(NotFoundException::new);
    }

    private User resolveAuthor(String userId) {
        return userRepository.findById(UserId.of(userId)).orElseThrow(NotFoundException::new);
    }
}
