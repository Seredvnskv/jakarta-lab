package org.com.recipes.recipe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.core.repository.Repository;
import org.com.recipes.recipe.Recipe;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class RecipeRepository implements Repository<Recipe, UUID> {

    private EntityManager em;

    protected RecipeRepository() {}

    @Inject
    public RecipeRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Recipe> findById(UUID id) {
        return Optional.ofNullable(em.find(Recipe.class, id));
    }

    @Override
    public List<Recipe> findAll() {
        return em.createQuery("SELECT r FROM Recipe r", Recipe.class).getResultList();
    }

    public List<Recipe> findByCuisineId(UUID cuisineId) {
        return em.createQuery("SELECT r FROM Recipe r WHERE r.cuisine.id = :cuisineId", Recipe.class)
                .setParameter("cuisineId", cuisineId)
                .getResultList();
    }

    @Override
    public Recipe save(Recipe recipe) {
        return em.merge(recipe);
    }

    @Override
    public void delete(Recipe recipe) {
        em.remove(em.merge(recipe));
    }

    public void deleteRecipe(UUID id) {
        delete(findById(id).orElseThrow(NotFoundException::new));
    }
}