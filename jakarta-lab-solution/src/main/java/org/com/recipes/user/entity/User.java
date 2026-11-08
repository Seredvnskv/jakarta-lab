package org.com.recipes.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.com.recipes.recipe.entity.Recipe;
import org.com.recipes.user.entity.id.UserId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private UserId id;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate registrationDate;

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private List<Recipe> recipes =  new ArrayList<>();

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.setAuthor(this);
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
        recipe.setAuthor(null);
    }
}
