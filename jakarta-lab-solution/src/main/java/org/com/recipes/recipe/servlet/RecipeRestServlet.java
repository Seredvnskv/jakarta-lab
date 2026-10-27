package org.com.recipes.recipe.servlet;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.recipes.core.dto.ListResponseDto;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.service.CuisineService;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.dto.CreateRecipeDto;
import org.com.recipes.recipe.mapper.RecipeMapper;
import org.com.recipes.recipe.service.RecipeService;
import org.com.recipes.user.User;
import org.com.recipes.user.service.UserService;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "RecipeRestServlet", urlPatterns = "/api/servlet/recipes/*")
public class RecipeRestServlet extends HttpServlet {
    private final RecipeService recipeService;
    private final CuisineService cuisineService;
    private final UserService userService;
    private final Jsonb jsonb;

    @Inject
    public RecipeRestServlet(RecipeService recipeService, CuisineService cuisineService, UserService userService, Jsonb jsonb) {
        this.recipeService = recipeService;
        this.cuisineService = cuisineService;
        this.userService = userService;
        this.jsonb = jsonb;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        if (path == null || path.equals("/")) {
            String cuisineId = req.getParameter("cuisineId");
            if (cuisineId != null) {
                jsonb.toJson(new ListResponseDto<>(recipeService.findRecipesByCuisine(UUID.fromString(cuisineId)).stream()
                        .map(RecipeMapper::toDto).toList()), resp.getWriter());
            }
            else {
                jsonb.toJson(new ListResponseDto<>(recipeService.findAllRecipes().stream().map(RecipeMapper::toDto).toList()), resp.getWriter());
            }
        }
        else {
            String id = path.substring(1);
            jsonb.toJson(RecipeMapper.toDto(recipeService.findRecipeById(UUID.fromString(id))), resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateRecipeDto dto = jsonb.fromJson(req.getInputStream(), CreateRecipeDto.class);
        Cuisine cuisine = cuisineService.findCuisineById(UUID.fromString(dto.cuisineId()));
        User author = userService.provideUser(UUID.fromString(dto.authorId()));
        Recipe recipe = RecipeMapper.toEntity(dto, cuisine, author);
        recipeService.createRecipe(recipe);
        resp.setContentType("application/json");
        resp.setHeader("Location", req.getServletPath() + "/" + recipe.getId());
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getPathInfo();
        recipeService.deleteRecipe(UUID.fromString(path.substring(1)));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
