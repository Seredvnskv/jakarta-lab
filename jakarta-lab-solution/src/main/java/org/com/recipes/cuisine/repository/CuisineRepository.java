package org.com.recipes.cuisine.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.com.recipes.core.repository.Repository;
import org.com.recipes.cuisine.entity.Cuisine;
import org.com.recipes.cuisine.entity.id.CuisineId;

import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
@Transactional
@NoArgsConstructor
public class CuisineRepository implements Repository<Cuisine, CuisineId> {

    private EntityManager entityManager;

    @Inject
    public CuisineRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Cuisine> findById(CuisineId id) {
        return Optional.ofNullable(entityManager.find(Cuisine.class, id));
    }

    @Override
    public Collection<Cuisine> findAll() {
        return entityManager.createQuery("select c from Cuisine c", Cuisine.class).getResultList();
    }

    @Override
    public Cuisine save(Cuisine entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(Cuisine entity) {
        entityManager.remove(entity);
    }
}
