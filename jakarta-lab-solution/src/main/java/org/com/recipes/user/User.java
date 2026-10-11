package org.com.recipes.user;

import lombok.*;
import org.com.recipes.recipe.Recipe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
public class User {
    private UUID id;
    private String username;
    private Role role;
    private LocalDate registrationDate;

    @EqualsAndHashCode.Exclude
    private List<Recipe> recipes =  new ArrayList<>();
}
