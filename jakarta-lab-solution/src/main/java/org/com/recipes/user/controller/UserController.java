package org.com.recipes.user.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.user.dto.UserDto;
import org.com.recipes.user.entity.id.UserId;
import org.com.recipes.user.service.UserService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private final UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    public CollectionRepresentation<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GET
    @Path("/{id}")
    public UserDto getUser(@PathParam("id") String id) {
        return userService.getUser(UserId.of(id));
    }

    @POST
    public Response createUser(UserDto dto, @Context UriInfo uriInfo) {
        UserDto created = userService.createUser(dto);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(created.id()).build())
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public UserDto updateUser(@PathParam("id") String id, UserDto dto) {
        return userService.updateUser(UserId.of(id), dto);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") String id) {
        userService.deleteUser(UserId.of(id));
        return Response.noContent().build();
    }
}
