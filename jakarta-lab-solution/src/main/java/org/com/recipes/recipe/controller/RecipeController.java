package org.com.recipes.recipe.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.recipe.dto.CreateRecipeDto;
import org.com.recipes.recipe.dto.RecipeDto;
import org.com.recipes.recipe.entity.id.RecipeId;
import org.com.recipes.recipe.service.RecipeService;

@Path("/recipes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecipeController {
    private final RecipeService recipeService;

    @Inject
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GET
    public CollectionRepresentation<RecipeDto> getRecipes() {
        return recipeService.getRecipes();
    }

    @GET
    @Path("/{id}")
    public RecipeDto getRecipe(@PathParam("id") String id) {
        return recipeService.getRecipe(RecipeId.of(id));
    }

    @POST
    public Response createRecipe(CreateRecipeDto dto, @Context UriInfo uriInfo) {
        RecipeDto created = recipeService.createRecipe(dto);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(created.id()).build())
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public RecipeDto updateRecipe(@PathParam("id") String id, CreateRecipeDto dto) {
        return recipeService.updateRecipe(RecipeId.of(id), dto);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRecipe(@PathParam("id") String id) {
        recipeService.deleteRecipe(RecipeId.of(id));
        return Response.noContent().build();
    }
}
