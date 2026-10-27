package org.com.recipes.cuisine.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.repository.CuisineRepository;
import org.com.recipes.recipe.service.RecipeService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CuisineService {

    private CuisineRepository cuisineRepository;
    private RecipeService recipeService;

    protected CuisineService() {}

    @Inject
    public CuisineService(CuisineRepository cuisineRepository, RecipeService recipeService) {
        this.cuisineRepository = cuisineRepository;
        this.recipeService = recipeService;
    }

    public List<Cuisine> findAllCuisines() {
        return cuisineRepository.findAll();
    }

    public Cuisine findCuisineById(UUID id) {
        return cuisineRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void createCuisine(Cuisine cuisine) {
        cuisineRepository.findById(cuisine.getId()).ifPresent(_ -> {
            throw new InvalidDataException("Cuisine already exists");
        });
        cuisineRepository.save(cuisine);
        System.out.println("Created cuisine: %s (%s)".formatted(cuisine.getName(), cuisine.getId()));
    }

    public void createAllCuisines(Collection<Cuisine> cuisines) {
        cuisineRepository.saveAll(cuisines);
    }

    public void deleteCuisine(UUID id) {
        recipeService.findRecipesByCuisine(id).forEach(recipe -> recipeService.deleteRecipe(recipe.getId()));
        cuisineRepository.deleteCuisine(id);
    }

    public void deleteAllCuisines() {
        cuisineRepository.deleteAll();
    }
}
