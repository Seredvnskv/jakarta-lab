package org.com.recipes.core.mapper;

import java.util.Collection;

public interface EntityMapper<E, D> {
    Collection<D> toDtos(Collection<E> entities);
    D toDto(E entity);
    E toEntity(D dto);
}
