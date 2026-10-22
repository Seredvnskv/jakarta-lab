package org.com.recipes.cuisine.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.core.repository.Repository;
import org.com.recipes.cuisine.Cuisine;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class CuisineRepository implements Repository<Cuisine, UUID> {

    private EntityManager em;

    protected CuisineRepository() {}

    @Inject
    public CuisineRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Cuisine> findById(UUID id) {
        return Optional.ofNullable(em.find(Cuisine.class, id));
    }

    @Override
    public List<Cuisine> findAll() {
        return em.createQuery("SELECT c FROM Cuisine c", Cuisine.class).getResultList();
    }

    @Override
    public Cuisine save(Cuisine cuisine) {
        return em.merge(cuisine);
    }

    @Override
    public void saveAll(Collection<Cuisine> e) {
        e.forEach(this::save);
    }

    @Override
    public void delete(Cuisine cuisine) {
        em.remove(em.merge(cuisine));
    }

    @Override
    public void deleteAll() {
        em.createQuery("DELETE FROM Cuisine").executeUpdate();
    }

    public void deleteCuisine(UUID id) {
        delete(findById(id).orElseThrow(NotFoundException::new));
    }
}
