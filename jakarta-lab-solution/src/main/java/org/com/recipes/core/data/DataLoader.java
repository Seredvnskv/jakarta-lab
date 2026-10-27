package org.com.recipes.core.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.BeforeDestroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.Region;
import org.com.recipes.cuisine.service.CuisineService;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.service.RecipeService;
import org.com.recipes.user.Role;
import org.com.recipes.user.User;
import org.com.recipes.user.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor
public class DataLoader {
    private UserService userService;
    private CuisineService cuisineService;
    private RecipeService recipeService;

    private final URL triss = this.getClass().getResource("/assets/triss.png");
    private final URL ciri = this.getClass().getResource("/assets/ciri.jpg");
    private final URL geralt = this.getClass().getResource("/assets/geralt.jpg");
    private final URL jaskier = this.getClass().getResource("/assets/jaskier.jpg");
    private final URL zoltan = this.getClass().getResource("/assets/zoltan.jpg");

    @Inject
    public DataLoader(UserService userService, CuisineService cuisineService, RecipeService recipeService) {
        this.userService = userService;
        this.cuisineService = cuisineService;
        this.recipeService = recipeService;
    }

    private final User userTriss = User.builder().id(UUID.fromString("6b04e739-3251-4f8a-a962-5e1d8b1a592d"))
            .username("Triss")
            .role(Role.ADMIN)
            .registrationDate(LocalDate.now())
            .avatar(provideAvatar(triss))
            .build();

    private final User userCiri = User.builder().id(UUID.fromString("58449fe0-5e39-42ab-b1a4-f94e8c50849d"))
            .username("Ciri").role(Role.USER)
            .registrationDate(LocalDate.now())
            .avatar(provideAvatar(ciri))
            .build();

    private final User userGeralt = User.builder().id(UUID.fromString("439af2a7-9196-429e-a02d-aedc09b66f96"))
            .username("Geralt").role(Role.USER)
            .registrationDate(LocalDate.now())
            .avatar(provideAvatar(geralt))
            .build();

    private final User userJaskier = User.builder().id(UUID.fromString("2ac72290-178b-495d-9c73-3fc3abe98533"))
            .username("Jaskier")
            .role(Role.USER)
            .registrationDate(LocalDate.now())
            .avatar(provideAvatar(jaskier))
            .build();

    private final User userZoltan = User.builder().id(UUID.fromString("8285f6d1-3dc9-46a6-9c5d-0484634e5458"))
            .username("Zoltan")
            .role(Role.USER)
            .registrationDate(LocalDate.now())
            .avatar(provideAvatar(zoltan))
            .build();

    private final Set<User> users = Set.of(userTriss, userCiri, userGeralt, userJaskier, userZoltan);

    private final Cuisine cuisineTemeria = Cuisine.builder().id(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0001"))
            .name("Temerian").region(Region.TEMERIA).spicy(false).build();

    private final Cuisine cuisineRedania = Cuisine.builder().id(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0002"))
            .name("Redanian").region(Region.REDANIA).spicy(false).build();

    private final Cuisine cuisineNilfgaard = Cuisine.builder().id(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0003"))
            .name("Nilfgaardian").region(Region.NILFGAARD).spicy(true).build();

    private final Cuisine cuisineToussaint = Cuisine.builder().id(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0004"))
            .name("Toussaintois").region(Region.TOUSSAINT).spicy(false).build();

    private final Cuisine cuisineSkellige = Cuisine.builder().id(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0005"))
            .name("Skelligan").region(Region.SKELLIGE).spicy(false).build();

    private final Cuisine cuisineKovir = Cuisine.builder().id(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0006"))
            .name("Kovirian").region(Region.KOVIR).spicy(false).build();

    private final Cuisine cuisineZerrikania = Cuisine.builder().id(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0007"))
            .name("Zerrikanian").region(Region.ZERRIKANIA).spicy(true).build();

    private final Set<Cuisine> cuisines = Set.of(cuisineTemeria, cuisineRedania, cuisineNilfgaard, cuisineToussaint,
            cuisineSkellige, cuisineKovir, cuisineZerrikania);

    private final Set<Recipe> recipes = Set.of(
            Recipe.builder().id(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01"))
                    .cuisine(cuisineTemeria)
                    .author(userGeralt)
                    .title("Bigos")
                    .description("A hearty hunter's stew of sauerkraut and smoked meats, favored by witchers resting between contracts.")
                    .ingredients(List.of("sauerkraut", "cabbage", "smoked bacon", "kielbasa", "wild boar meat", "prunes", "juniper berries", "onion"))
                    .servings(4)
                    .caloriesPerServing(650)
                    .vegetarian(false)
                    .creationDate(LocalDate.now())
                    .build(),

            Recipe.builder().id(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02"))
                    .cuisine(cuisineRedania)
                    .author(userTriss)
                    .title("Zurek")
                    .description("Tangy fermented rye soup served in a bread bowl, warming for long rides across Redania.")
                    .ingredients(List.of("fermented rye starter", "white sausage", "potatoes", "marjoram", "garlic", "hard-boiled egg"))
                    .servings(4)
                    .caloriesPerServing(420)
                    .vegetarian(false)
                    .creationDate(LocalDate.now())
                    .build(),

            Recipe.builder().id(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03"))
                    .cuisine(cuisineKovir)
                    .author(userCiri)
                    .title("Pierogi Ruskie")
                    .description("Dumplings stuffed with potato and curd cheese, a traveler's favorite on the road to Kovir.")
                    .ingredients(List.of("potatoes", "twarog cheese", "onion", "flour", "butter"))
                    .servings(6)
                    .caloriesPerServing(380)
                    .vegetarian(true)
                    .creationDate(LocalDate.now())
                    .build(),

            Recipe.builder().id(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04"))
                    .cuisine(cuisineNilfgaard)
                    .author(userZoltan)
                    .title("Nilfgaardian Roast Boar")
                    .description("Slow-roasted boar marinated in imperial red wine, served at a Nilfgaardian officers' table.")
                    .ingredients(List.of("wild boar leg", "rosemary", "garlic", "red wine", "root vegetables"))
                    .servings(8)
                    .caloriesPerServing(720)
                    .vegetarian(false)
                    .creationDate(LocalDate.now())
                    .build(),

            Recipe.builder().id(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05"))
                    .cuisine(cuisineSkellige)
                    .author(userJaskier)
                    .title("Skellige Herring Platter")
                    .description("Salted herring and rye, the staple of Skellige longship crews.")
                    .ingredients(List.of("pickled herring", "rye bread", "onion", "sour cream", "dill"))
                    .servings(2)
                    .caloriesPerServing(310)
                    .vegetarian(false)
                    .creationDate(LocalDate.now())
                    .build(),

            Recipe.builder().id(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a06"))
                    .cuisine(cuisineToussaint)
                    .author(userTriss)
                    .title("Toussaint Duck with Blood Wine Reduction")
                    .description("Duck glazed in Toussaint's famed blood-red wine, fit for a knight's feast.")
                    .ingredients(List.of("duck breast", "Est Est blood wine", "honey", "thyme", "shallots"))
                    .servings(2)
                    .caloriesPerServing(540)
                    .vegetarian(false)
                    .creationDate(LocalDate.now())
                    .build(),

            Recipe.builder().id(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a07"))
                    .cuisine(cuisineZerrikania)
                    .author(userGeralt)
                    .title("Zerrikanian Spiced Lamb Skewers")
                    .description("Fire-charred lamb skewers rubbed with exotic Zerrikanian spices.")
                    .ingredients(List.of("lamb shoulder", "cumin", "chili", "yogurt", "saffron", "lemon"))
                    .servings(4)
                    .caloriesPerServing(480)
                    .vegetarian(false)
                    .creationDate(LocalDate.now())
                    .build(),

            Recipe.builder().id(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a08"))
                    .cuisine(cuisineTemeria)
                    .author(userCiri)
                    .title("Krupnik")
                    .description("A simple barley soup, the kind served in every Temerian village inn.")
                    .ingredients(List.of("barley", "potatoes", "carrots", "celery", "butter", "marjoram"))
                    .servings(4)
                    .caloriesPerServing(290)
                    .vegetarian(true)
                    .creationDate(LocalDate.now())
                    .build()
    );

    public void load(@Observes @Initialized(ApplicationScoped.class) Object init) {
        if (userService.provideUsers().isEmpty()) {
            userService.createAllUsers(users);
        }
        if (cuisineService.findAllCuisines().isEmpty()) {
            cuisineService.createAllCuisines(cuisines);
        }
        if (recipeService.findAllRecipes().isEmpty()) {
            recipeService.createAllRecipes(recipes);
        }
    }

    public void clean(@Observes @BeforeDestroyed(ApplicationScoped.class) Object init) {
        recipeService.deleteAllRecipes();
        cuisineService.deleteAllCuisines();
        userService.deleteAllUsers();
    }

    private byte[] provideAvatar(URL resource) {
        try (InputStream in = resource.openStream()) {
            return in.readAllBytes();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
