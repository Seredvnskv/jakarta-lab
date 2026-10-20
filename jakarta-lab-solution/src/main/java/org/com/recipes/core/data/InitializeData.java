package org.com.recipes.core.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.BeforeDestroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.com.recipes.cuisine.Continent;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.service.CuisineService;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.service.RecipeService;
import org.com.recipes.user.Role;
import org.com.recipes.user.User;
import org.com.recipes.user.service.UserService;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public final class InitializeData {

    private final UserService userService;
    private final CuisineService cuisineService;
    private final RecipeService recipeService;

    private final URL trissAvatar = this.getClass().getResource("/assets/triss.png");
    private final URL ciriAvatar = this.getClass().getResource("/assets/ciri.jpg");
    private final URL geraltAvatar = this.getClass().getResource("/assets/geralt.jpg");
    private final URL jaskierAvatar = this.getClass().getResource("/assets/jaskier.jpg");
    private final URL zoltanAvatar = this.getClass().getResource("/assets/zoltan.jpg");

    private static final UUID ITALIAN_ID = UUID.fromString("a2f4a5c1-6b9e-4b7d-8f0e-1c2d3e4f5a6b");
    private static final UUID MEXICAN_ID = UUID.fromString("b3f5b6d2-7c0f-4c8e-9f1f-2d3e4f5a6b7c");

    @Inject
    public InitializeData(UserService userService, CuisineService cuisineService, RecipeService recipeService) {
        this.userService = userService;
        this.cuisineService = cuisineService;
        this.recipeService = recipeService;
    }

    public void init(@Observes @Initialized(ApplicationScoped.class) Object event) {
        recipeService.findAllRecipes().forEach(recipe -> recipeService.deleteRecipe(recipe.getId()));
        cuisineService.findAllCuisines().forEach(cuisine -> cuisineService.deleteCuisine(cuisine.getId()));
        userService.findAllUsers().forEach(user -> userService.deleteUser(user.getId()));

        if (userService.findAllUsers().isEmpty()) {
            createUsers();
        }
        if (cuisineService.findAllCuisines().isEmpty()) {
            createCuisines();
        }
        if (recipeService.findAllRecipes().isEmpty()) {
            createRecipes();
        }

        demonstrateCuisineToRecipes();
    }

    private void demonstrateCuisineToRecipes() {
        logRecipesForCuisine(cuisineService.findCuisineById(ITALIAN_ID));
        logRecipesForCuisine(cuisineService.findCuisineById(MEXICAN_ID));
    }

    private void logRecipesForCuisine(Cuisine cuisine) {
        List<String> titles = recipeService.findRecipesByCuisine(cuisine.getId()).stream()
                .map(Recipe::getTitle)
                .toList();
        System.out.println("Cuisine %s has %d recipe(s): %s".formatted(cuisine.getName(), titles.size(), titles));
    }

    private void createUsers() {
        User triss = User.builder()
                .id(UUID.fromString("d1edfd91-3bf6-451b-8cfd-fed9b23f3278"))
                .role(Role.ADMIN)
                .username("Triss")
                .registrationDate(LocalDate.now())
                .avatar(provideAvatar(trissAvatar))
                .build();

        User ciri = User.builder()
                .id(UUID.fromString("ce591e43-1b49-4d49-9a02-015a7ad00dd1"))
                .role(Role.USER)
                .username("Ciri")
                .registrationDate(LocalDate.now())
                .avatar(provideAvatar(ciriAvatar))
                .build();

        User geralt = User.builder()
                .id(UUID.fromString("f4274889-1f42-4ac2-abf5-a6a7e53234fc"))
                .role(Role.USER)
                .username("Geralt")
                .registrationDate(LocalDate.now())
                .avatar(provideAvatar(geraltAvatar))
                .build();

        User jaskier = User.builder()
                .id(UUID.fromString("93ca65b5-47c3-42dd-bbd5-b3947326fdf4"))
                .role(Role.USER)
                .username("Jaskier")
                .registrationDate(LocalDate.now())
                .avatar(provideAvatar(jaskierAvatar))
                .build();

        User zoltan = User.builder()
                .id(UUID.fromString("60c2bd65-4f21-426d-91f3-070c2f867336"))
                .role(Role.USER)
                .username("Zoltan")
                .registrationDate(LocalDate.now())
                .avatar(provideAvatar(zoltanAvatar))
                .build();

        userService.createUser(triss);
        userService.createUser(ciri);
        userService.createUser(geralt);
        userService.createUser(jaskier);
        userService.createUser(zoltan);
    }

    private void createCuisines() {
        Cuisine italian = Cuisine.builder()
                .id(ITALIAN_ID)
                .name("Italian")
                .continent(Continent.EUROPE)
                .spicy(false)
                .build();

        Cuisine mexican = Cuisine.builder()
                .id(MEXICAN_ID)
                .name("Mexican")
                .continent(Continent.AMERICA)
                .spicy(true)
                .build();

        cuisineService.createCuisine(italian);
        cuisineService.createCuisine(mexican);
    }

    private void createRecipes() {
        Cuisine italian = cuisineService.findCuisineById(ITALIAN_ID);
        Cuisine mexican = cuisineService.findCuisineById(MEXICAN_ID);

        Recipe margheritaPizza = Recipe.builder()
                .id(UUID.fromString("c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f"))
                .cuisine(italian)
                .title("Margherita Pizza")
                .description("Classic Neapolitan pizza with tomato, mozzarella and fresh basil.")
                .ingredients(List.of("Pizza dough", "Tomato sauce", "Mozzarella", "Fresh basil", "Olive oil"))
                .servings(4)
                .caloriesPerServing(266)
                .vegetarian(true)
                .creationDate(LocalDate.now())
                .build();

        Recipe spaghettiCarbonara = Recipe.builder()
                .id(UUID.fromString("d2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a"))
                .cuisine(italian)
                .title("Spaghetti Carbonara")
                .description("Roman pasta dish with eggs, cheese, guanciale and black pepper.")
                .ingredients(List.of("Spaghetti", "Eggs", "Pecorino Romano", "Guanciale", "Black pepper"))
                .servings(4)
                .caloriesPerServing(430)
                .vegetarian(false)
                .creationDate(LocalDate.now())
                .build();

        Recipe tacosAlPastor = Recipe.builder()
                .id(UUID.fromString("e3f4a5b6-c7d8-4e9f-0a1b-2c3d4e5f6a7b"))
                .cuisine(mexican)
                .title("Tacos al Pastor")
                .description("Marinated pork tacos with pineapple, onion and cilantro.")
                .ingredients(List.of("Pork shoulder", "Achiote paste", "Pineapple", "Corn tortillas", "White onion", "Cilantro"))
                .servings(4)
                .caloriesPerServing(310)
                .vegetarian(false)
                .creationDate(LocalDate.now())
                .build();

        recipeService.createRecipe(margheritaPizza);
        recipeService.createRecipe(spaghettiCarbonara);
        recipeService.createRecipe(tacosAlPastor);
    }

    private byte[] provideAvatar(URL resource) {
        try (InputStream in = resource.openStream()) {
            return in.readAllBytes();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
