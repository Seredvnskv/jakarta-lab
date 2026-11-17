package org.com.recipes.recipe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.com.recipes.core.repository.Repository;
import org.com.recipes.cuisine.entity.Cuisine;
import org.com.recipes.user.entity.User;
import org.com.recipes.recipe.entity.Recipe;
import org.com.recipes.recipe.entity.id.RecipeId;

import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
@Transactional
@NoArgsConstructor
public class RecipeRepository implements Repository<Recipe, RecipeId> {

    private EntityManager entityManager;

    @Inject
    public RecipeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Recipe> findById(RecipeId id) {
        return Optional.ofNullable(entityManager.find(Recipe.class, id));
    }

    @Override
    public Collection<Recipe> findAll() {
        return entityManager.createQuery("select r from Recipe r", Recipe.class).getResultList();
    }

    public Collection<Recipe> findAllByAuthor(User author) {
        return entityManager.createQuery("select r from Recipe r where r.author = :author", Recipe.class)
                .setParameter("author", author)
                .getResultList();
    }

    public Optional<Recipe> findByIdAndAuthor(RecipeId id, User author) {
        return entityManager.createQuery("select r from Recipe r where r.id = :id and r.author = :author", Recipe.class)
                .setParameter("id", id)
                .setParameter("author", author)
                .getResultStream()
                .findFirst();
    }

    public Collection<Recipe> findAllByCuisineAndAuthor(Cuisine cuisine, User author) {
        return entityManager.createQuery("select r from Recipe r where r.cuisine = :cuisine and r.author = :author", Recipe.class)
                .setParameter("cuisine", cuisine)
                .setParameter("author", author)
                .getResultList();
    }

    @Override
    public Recipe save(Recipe entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(Recipe entity) {
        entityManager.remove(entity);
    }
}
