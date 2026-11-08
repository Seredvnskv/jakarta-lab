package org.com.recipes.recipe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.com.recipes.cuisine.entity.Cuisine;
import org.com.recipes.recipe.entity.id.RecipeId;
import org.com.recipes.user.entity.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Recipe {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private RecipeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuisine_id", referencedColumnName = "id")
    private Cuisine cuisine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"))
    @Builder.Default
    Set<Ingredient> ingredients = new HashSet<>();

    private String title;
    private String description;
    private int servings;
    private int calories;
    private boolean vegetarian;
    private LocalDateTime creationTime;
}
