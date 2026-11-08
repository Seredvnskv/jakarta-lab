package org.com.recipes.cuisine.entity;

import jakarta.persistence.*;
import lombok.*;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.recipe.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuisines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cuisine {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private CuisineId id;

    private Continent continent;
    private String name;
    private boolean spicy;

    @OneToMany(mappedBy = "cuisine", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Recipe> recipes = new ArrayList<>();

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.setCuisine(this);
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
        recipe.setCuisine(null);
    }
}
