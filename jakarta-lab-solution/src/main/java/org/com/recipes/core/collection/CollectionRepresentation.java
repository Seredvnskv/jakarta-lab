package org.com.recipes.core.collection;

import java.time.LocalDateTime;
import java.util.Collection;

public record CollectionRepresentation<T>(Collection<T> items, int size, LocalDateTime time) {
    public static <T> CollectionRepresentation<T> of(Collection<T> collection) {
        return new CollectionRepresentation<>(collection, collection.size(), LocalDateTime.now());
    }
}
