package org.com.recipes.recipe;

import lombok.*;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.user.User;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
public class Recipe {
    private UUID id;

    @EqualsAndHashCode.Exclude
    private Cuisine cuisine;

    @EqualsAndHashCode.Exclude
    private User author;

    private String title;
    private String description;
    private int servings;
    private double caloriesPerServing;
    private boolean vegetarian;
    private LocalDate creationDate;
}
