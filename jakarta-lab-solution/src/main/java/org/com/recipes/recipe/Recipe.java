package org.com.recipes.recipe;

import jakarta.persistence.*;
import lombok.*;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
public class Recipe {

    @Id
    private UUID id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    private Cuisine cuisine;

    @Transient
    @EqualsAndHashCode.Exclude
    private User author;

    @ElementCollection
    @Builder.Default
    List<String> ingredients = new ArrayList<>();
    private String title;
    private String description;
    private int servings;
    private double caloriesPerServing;
    private boolean vegetarian;
    private LocalDate creationDate;
}
