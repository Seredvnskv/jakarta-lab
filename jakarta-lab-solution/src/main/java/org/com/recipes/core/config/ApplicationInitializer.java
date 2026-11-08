package org.com.recipes.core.config;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.service.CuisineService;
import org.com.recipes.recipe.dto.CreateRecipeDto;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.entity.Ingredient;
import org.com.recipes.recipe.entity.id.RecipeId;
import org.com.recipes.recipe.service.RecipeService;
import org.com.recipes.user.dto.UserDto;
import org.com.recipes.user.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Singleton
@Startup
@Log
@NoArgsConstructor
public class ApplicationInitializer {
    private UserService userService;
    private RecipeService recipeService;
    private CuisineService cuisineService;

    @Inject
    public ApplicationInitializer(UserService userService, RecipeService recipeService, CuisineService cuisineService) {
        this.userService = userService;
        this.recipeService = recipeService;
        this.cuisineService = cuisineService;
    }

    private final List<String> cuisineIds = new ArrayList<>();
    private final List<String> userIds = new ArrayList<>();
    private final List<String> recipeIds = new ArrayList<>();

    @PostConstruct
    public void init() {
        clearExistingData();

        UserDto triss = createUser("0ceb983a-1529-40c7-9d83-dc29acae7b55", "Triss", "ADMIN", LocalDate.of(2023, 1, 10));
        UserDto ciri = createUser("25ea3783-7cf8-41d7-b9e6-fdb1760dacae", "Ciri", "USER", LocalDate.of(2024, 3, 22));
        UserDto geralt = createUser("3802ef10-9f04-41cb-a3ad-3729a9da5c7a", "Geralt", "USER", LocalDate.of(2025, 6, 5));
        UserDto zoltan = createUser("deb9a5f8-fec3-4d98-b8fb-f78965e16700", "Zoltan", "USER", LocalDate.of(2026, 6, 5));

        CuisineDto italian = createCuisine("0b5a2470-efa8-4b61-a106-70663055ad9a", "EUROPE", "Italian", false);
        CuisineDto mexican = createCuisine("acb2ccef-e60f-462f-896f-5a7a3ac8c15d", "AMERICA", "Mexican", true);
        CuisineDto thai = createCuisine("c5a72611-0b8b-40f8-85a6-28389c3b4a4d", "ASIA", "Thai", true);
        CuisineDto polish = createCuisine("01b14723-fe2a-4bd6-a63e-3cdc5c34df7d", "EUROPE", "Polish", false);
        CuisineDto japanese = createCuisine("726537f2-48bf-4275-adb8-f5188bef27f5", "ASIA", "Japanese", false);

        createRecipe("Margherita Pizza", "Classic Neapolitan pizza with tomato, mozzarella and basil.", italian.id(), triss.id(),
                Set.of(new Ingredient("Tomato", 200, "g"), new Ingredient("Mozzarella", 150, "g"), new Ingredient("Basil", 5, "leaves")),
                4, 800, true);

        createRecipe("Tacos al Pastor", "Marinated pork tacos with pineapple and tortillas.", mexican.id(), geralt.id(),
                Set.of(new Ingredient("Pork", 500, "g"), new Ingredient("Pineapple", 100, "g"), new Ingredient("Tortilla", 8, "pcs")),
                4, 650, false);

        createRecipe("Pad Thai", "Stir-fried rice noodles with shrimp and peanuts.", thai.id(), ciri.id(),
                Set.of(new Ingredient("Rice noodles", 250, "g"), new Ingredient("Shrimp", 200, "g"), new Ingredient("Peanuts", 50, "g")),
                2, 550, false);

        createRecipe("Green Curry", "Thai curry with chicken and coconut milk.", thai.id(), triss.id(),
                Set.of(new Ingredient("Chicken", 300, "g"), new Ingredient("Coconut milk", 400, "ml"), new Ingredient("Green curry paste", 2, "tbsp")),
                3, 700, false);

        createRecipe("Pierogi ruskie", "Dumplings filled with potato and cottage cheese.", polish.id(), zoltan.id(),
                Set.of(new Ingredient("Potato", 400, "g"), new Ingredient("Twaróg", 200, "g"), new Ingredient("Onion", 100, "g")),
                4, 600, true);

        createRecipe("Bigos", "Traditional Polish hunter's stew with sauerkraut and kielbasa.", polish.id(), geralt.id(),
                Set.of(new Ingredient("Sauerkraut", 500, "g"), new Ingredient("Kielbasa", 300, "g"), new Ingredient("Dried plums", 50, "g")),
                6, 750, false);

        createRecipe("Ramen", "Japanese noodle soup with pork belly and soy sauce broth.", japanese.id(), ciri.id(),
                Set.of(new Ingredient("Noodles", 200, "g"), new Ingredient("Pork belly", 150, "g"), new Ingredient("Soy sauce", 30, "ml")),
                2, 650, false);

        log.info(() -> "Seeded " + cuisineIds.size() + " cuisines, " + userIds.size() + " users, " + recipeIds.size() + " recipes.");
    }

    private void clearExistingData() {
        recipeService.getRecipes().items().forEach(recipe -> recipeService.deleteRecipe(RecipeId.of(recipe.id())));
    }

    private CuisineDto createCuisine(String id, String continent, String name, boolean spicy) {
        CuisineDto dto = CuisineDto.builder()
                .id(id)
                .continent(continent)
                .name(name)
                .spicy(spicy)
                .build();
        CuisineDto saved = cuisineService.createCuisine(dto);
        cuisineIds.add(saved.id());
        return saved;
    }

    private UserDto createUser(String id, String username, String role, LocalDate registrationDate) {
        UserDto dto = UserDto.builder()
                .id(id)
                .username(username)
                .role(role)
                .registrationDate(registrationDate)
                .build();
        UserDto saved = userService.createUser(dto);
        userIds.add(saved.id());
        return saved;
    }

    private void createRecipe(String title, String description, String cuisineId, String authorId, Set<Ingredient> ingredients, int servings, int calories, boolean vegetarian) {
        CreateRecipeDto dto = new CreateRecipeDto(title, description, cuisineId, authorId, ingredients, servings, calories, vegetarian);
        RecipeDto saved = recipeService.createRecipe(dto);
        recipeIds.add(saved.id());
    }
}
