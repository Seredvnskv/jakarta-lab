package org.com.recipes.cuisine.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.dto.CreateCuisineDto;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.mapper.CuisineMapper;
import org.com.recipes.cuisine.service.CuisineService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("cuisine")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuisineController {

    private final CuisineService cuisineService;

    @Inject
    public CuisineController(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @GET
    public Response getCuisines() {
        List<CuisineDto> cuisines = CuisineMapper.toDto(cuisineService.findAllCuisines());
        return Response
                .status(Response.Status.OK)
                .entity(cuisines)
                .build();
    }

    @GET
    @Path("{id}")
    public Response getCuisine(@PathParam("id") UUID id) {
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(CuisineMapper.toDto(cuisineService.findCuisineById(id)))
                    .build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createCuisine(CreateCuisineDto dto) {
        try {
            Cuisine cuisine = cuisineService.createCuisine(CuisineMapper.toEntity(dto));
            return Response.created(URI.create("/api/cuisine/" + cuisine.getId())).entity(CuisineMapper.toDto(cuisine)).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response updateCuisine(@PathParam("id") UUID id, CreateCuisineDto dto) {
        try {
            Cuisine cuisine = cuisineService.updateCuisine(CuisineMapper.toEntity(id, dto));
            return Response.ok(CuisineMapper.toDto(cuisine)).build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteCuisine(@PathParam("id") UUID id) {
        try {
            cuisineService.deleteCuisine(id);
            return Response.noContent().build();
        }
        catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
