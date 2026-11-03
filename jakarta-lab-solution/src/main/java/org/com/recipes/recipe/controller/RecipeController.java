package org.com.recipes.recipe.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.service.CuisineService;
import org.com.recipes.recipe.Recipe;
import org.com.recipes.recipe.dto.CreateRecipeDto;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.mapper.RecipeMapper;
import org.com.recipes.recipe.service.RecipeService;
import org.com.recipes.user.User;
import org.com.recipes.user.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("cuisine/{cuisineId}/recipe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecipeController {

    private final CuisineService cuisineService;
    private final RecipeService recipeService;
    private final UserService userService;

    @Inject
    public RecipeController(CuisineService cuisineService, RecipeService recipeService, UserService userService) {
        this.cuisineService = cuisineService;
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GET
    public Response getRecipes(@PathParam("cuisineId") UUID cuisineId) {
        try {
            cuisineService.findCuisineById(cuisineId);
            List<RecipeDto> recipes = RecipeMapper.toDtos(recipeService.findRecipesByCuisine(cuisineId));
            return Response.ok(recipes).build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("{id}")
    public Response getRecipe(@PathParam("cuisineId") UUID cuisineId, @PathParam("id") UUID id) {
        try {
            Recipe recipe = findInCuisine(cuisineId, id);
            return Response.ok(RecipeMapper.toDto(recipe)).build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createRecipe(@PathParam("cuisineId") UUID cuisineId, CreateRecipeDto dto) {
        try {
            Cuisine cuisine = cuisineService.findCuisineById(cuisineId);
            User author = userService.provideUser(UUID.fromString(dto.authorId()));
            Recipe recipe = recipeService.createRecipe(RecipeMapper.toEntity(dto, cuisine, author));
            return Response.created(URI.create("/api/cuisine/" + cuisineId + "/recipe/" + recipe.getId()))
                    .entity(RecipeMapper.toDto(recipe))
                    .build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response updateRecipe(@PathParam("cuisineId") UUID cuisineId, @PathParam("id") UUID id, CreateRecipeDto dto) {
        try {
            Recipe recipe = findInCuisine(cuisineId, id);
            User author = userService.provideUser(UUID.fromString(dto.authorId()));
            Recipe updated = recipeService.updateRecipe(RecipeMapper.applyUpdate(recipe, dto, author));
            return Response.ok(RecipeMapper.toDto(updated)).build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteRecipe(@PathParam("cuisineId") UUID cuisineId, @PathParam("id") UUID id) {
        try {
            findInCuisine(cuisineId, id);
            recipeService.deleteRecipe(id);
            return Response.noContent().build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private Recipe findInCuisine(UUID cuisineId, UUID id) {
        Recipe recipe = recipeService.findRecipeById(id);
        if (!recipe.getCuisine().getId().equals(cuisineId)) {
            throw new NotFoundException();
        }
        return recipe;
    }
}
