package org.com.recipes.core.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<E, K> {
    Optional<E> findById(K id);
    List<E> findAll();
    E save(E e);
    void delete(E e);
}
