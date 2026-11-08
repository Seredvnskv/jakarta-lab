package org.com.recipes.cuisine.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.cuisine.service.CuisineService;

@Path("/cuisines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuisineController {
    private final CuisineService cuisineService;

    @Inject
    public CuisineController(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @GET
    public CollectionRepresentation<CuisineDto> getCuisines() {
        return cuisineService.getCuisines();
    }

    @GET
    @Path("/{id}")
    public CuisineDto getCuisine(@PathParam("id") String id) {
        return cuisineService.getCuisine(CuisineId.of(id));
    }

    @POST
    public Response createCuisine(CuisineDto dto, @Context UriInfo uriInfo) {
        CuisineDto created = cuisineService.createCuisine(dto);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(created.id()).build())
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public CuisineDto updateCuisine(@PathParam("id") String id, CuisineDto dto) {
        return cuisineService.updateCuisine(CuisineId.of(id), dto);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCuisine(@PathParam("id") String id) {
        cuisineService.deleteCuisine(CuisineId.of(id));
        return Response.noContent().build();
    }
}
