package org.com.recipes.cuisine;

import lombok.*;
import org.com.recipes.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
public class Cuisine {

    private UUID id;

    private Continent continent;
    private String name;
    private boolean spicy;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<Recipe> recipes = new ArrayList<>();
}
