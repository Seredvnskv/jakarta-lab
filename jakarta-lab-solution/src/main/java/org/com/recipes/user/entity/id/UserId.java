package org.com.recipes.user.entity.id;

import jakarta.persistence.Embeddable;
import org.com.recipes.core.entity.EntityId;

import java.util.UUID;

@Embeddable
public record UserId(UUID id) implements EntityId<UUID> {

    public static UserId random() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(String id) {
        return new UserId(UUID.fromString(id));
    }
}
