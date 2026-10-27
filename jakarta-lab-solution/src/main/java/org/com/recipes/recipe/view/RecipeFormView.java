package org.com.recipes.recipe.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.service.CuisineService;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.service.RecipeService;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
@Getter
@Setter
public class RecipeFormView implements Serializable {

    private final CuisineService cuisineService;
    private final RecipeService recipeService;

    private String cuisineId;
    private String recipeId;

    private String title;
    private String description;
    private int servings;
    private double caloriesPerServing;
    private boolean vegetarian;
    private String ingredientsText;
    private Cuisine selectedCuisine;

    private List<Cuisine> allCuisines;

    @Inject
    public RecipeFormView(CuisineService cuisineService, RecipeService recipeService) {
        this.cuisineService = cuisineService;
        this.recipeService = recipeService;
    }

    public void init() {
        allCuisines = cuisineService.findAllCuisines();

        if (isEditMode()) {
            Recipe recipe = recipeService.findRecipeById(UUID.fromString(recipeId));
            title = recipe.getTitle();
            description = recipe.getDescription();
            servings = recipe.getServings();
            caloriesPerServing = recipe.getCaloriesPerServing();
            vegetarian = recipe.isVegetarian();
            ingredientsText = String.join("\n", recipe.getIngredients());
            selectedCuisine = recipe.getCuisine();
        }
        else {
            selectedCuisine = cuisineService.findCuisineById(UUID.fromString(cuisineId));
        }
    }

    public boolean isEditMode() {
        return recipeId != null && !recipeId.isBlank();
    }

    public String save() {
        List<String> ingredients = ingredientsText == null ? List.of() : ingredientsText.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();

        if (isEditMode()) {
            Recipe recipe = recipeService.findRecipeById(UUID.fromString(recipeId));
            recipe.setTitle(title);
            recipe.setDescription(description);
            recipe.setServings(servings);
            recipe.setCaloriesPerServing(caloriesPerServing);
            recipe.setVegetarian(vegetarian);
            recipe.setIngredients(ingredients);
            recipe.setCuisine(selectedCuisine);
            recipeService.updateRecipe(recipe);
        }
        else {
            Recipe recipe = Recipe.builder()
                    .id(UUID.randomUUID())
                    .cuisine(selectedCuisine)
                    .ingredients(ingredients)
                    .title(title)
                    .description(description)
                    .servings(servings)
                    .caloriesPerServing(caloriesPerServing)
                    .vegetarian(vegetarian)
                    .creationDate(LocalDate.now())
                    .build();
            recipeService.createRecipe(recipe);
        }

        return "/cuisines/cuisines_view?faces-redirect=true&cuisineId=" + selectedCuisine.getId();
    }

    public String cancel() {
        return "/cuisines/cuisines_view?faces-redirect=true&cuisineId=" + cuisineId;
    }
}
