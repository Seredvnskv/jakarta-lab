package org.com.recipes.cuisine.entity.id;

import jakarta.persistence.Embeddable;
import org.com.recipes.core.entity.EntityId;

import java.util.UUID;

@Embeddable
public record CuisineId(UUID id) implements EntityId<UUID> {

    public static CuisineId random() {
        return new CuisineId(UUID.randomUUID());
    }

    public static CuisineId of(String id) {
        return new CuisineId(UUID.fromString(id));
    }
}
