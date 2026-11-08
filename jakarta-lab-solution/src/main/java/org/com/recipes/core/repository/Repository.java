package org.com.recipes.core.repository;

import java.util.Collection;
import java.util.Optional;

public interface Repository<E, K> {
    Optional<E> findById(K id);
    Collection<E> findAll();
    E save(E entity);
    void delete(E entity);
}
