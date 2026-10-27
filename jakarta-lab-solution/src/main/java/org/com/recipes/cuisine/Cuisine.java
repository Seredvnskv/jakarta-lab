package org.com.recipes.cuisine;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cuisines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
public class Cuisine {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Continent continent;
    private String name;
    private boolean spicy;
}
