package org.com.recipes.user;

import jakarta.persistence.*;
import lombok.*;
import org.com.recipes.recipe.Recipe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
public class User {

    @Id
    private UUID id;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate registrationDate;

    @Transient
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<Recipe> recipes =  new ArrayList<>();

    @EqualsAndHashCode.Exclude
    private byte[] avatar;
}
