package org.com.recipes.recipe.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.service.CuisineService;
import org.com.recipes.recipe.dto.CreateRecipeDto;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.entity.Ingredient;
import org.com.recipes.recipe.entity.id.RecipeId;
import org.com.recipes.recipe.service.RecipeService;
import org.com.recipes.user.dto.UserDto;
import org.com.recipes.user.service.UserService;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Named
@ViewScoped
@NoArgsConstructor
public class RecipeFormView implements Serializable {
    private static final Pattern INGREDIENT_LINE = Pattern.compile("^(.*?),\\s*([0-9]+(?:\\.[0-9]+)?)\\s*,\\s*(.*)$");

    private CuisineService cuisineService;
    private UserService userService;
    private RecipeService recipeService;

    @Inject
    public RecipeFormView(CuisineService cuisineService, UserService userService, RecipeService recipeService) {
        this.cuisineService = cuisineService;
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String presetCuisineId;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private CuisineDto cuisine;

    @Getter
    @Setter
    private String authorId;

    @Getter
    @Setter
    private int servings;

    @Getter
    @Setter
    private int calories;

    @Getter
    @Setter
    private boolean vegetarian;

    @Getter
    @Setter
    private String ingredientsText;

    @Getter
    private Collection<CuisineDto> cuisines;

    @Getter
    private Collection<UserDto> authors;

    public void init() {
        cuisines = cuisineService.getCuisines().items();
        authors = userService.getUsers().items();

        if (isEditing()) {
            RecipeDto recipe = recipeService.getRecipe(RecipeId.of(id));
            title = recipe.title();
            description = recipe.description();
            servings = recipe.servings();
            calories = recipe.calories();
            vegetarian = recipe.vegetarian();
            ingredientsText = formatIngredients(recipe.ingredients());
            cuisine = cuisines.stream().filter(c -> c.id().equals(recipe.cuisine())).findFirst().orElse(null);
            authorId = recipe.author();
        } else if (presetCuisineId != null && !presetCuisineId.isBlank()) {
            cuisine = cuisines.stream().filter(c -> c.id().equals(presetCuisineId)).findFirst().orElse(null);
        }
    }

    public boolean isEditing() {
        return id != null && !id.isBlank();
    }

    public String save() {
        CreateRecipeDto dto = new CreateRecipeDto(title, description, cuisine.id(), authorId,
                parseIngredients(ingredientsText), servings, calories, vegetarian);
        RecipeDto saved = isEditing()
                ? recipeService.updateRecipe(RecipeId.of(id), dto)
                : recipeService.createRecipe(dto);
        return "/recipes/recipe-details-view?faces-redirect=true&id=" + saved.id();
    }

    public String cancel() {
        return isEditing()
                ? "/recipes/recipe-details-view?faces-redirect=true&id=" + id
                : "/recipes/recipe-list-view?faces-redirect=true";
    }

    private static Set<Ingredient> parseIngredients(String text) {
        Set<Ingredient> result = new HashSet<>();
        if (text == null) {
            return result;
        }
        for (String line : text.split("\\R")) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            Matcher matcher = INGREDIENT_LINE.matcher(line);
            if (matcher.matches()) {
                result.add(new Ingredient(matcher.group(1).trim(), Double.parseDouble(matcher.group(2)), matcher.group(3).trim()));
            }
        }
        return result;
    }

    private static String formatIngredients(Collection<Ingredient> ingredients) {
        return ingredients.stream()
                .map(i -> i.getName() + ", " + i.getQuantity() + ", " + i.getUnit())
                .collect(Collectors.joining("\n"));
    }
}
