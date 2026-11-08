package org.com.recipes.recipe.entity.id;

import jakarta.persistence.Embeddable;
import org.com.recipes.core.entity.EntityId;

import java.util.UUID;

@Embeddable
public record RecipeId(UUID id) implements EntityId<UUID> {

    public static RecipeId random() {
        return new RecipeId(UUID.randomUUID());
    }

    public static RecipeId of(String id) {
        return new RecipeId(UUID.fromString(id));
    }
}
