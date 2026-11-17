package org.com.recipes.user.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.user.dto.RegisterUserDto;
import org.com.recipes.user.dto.UserDto;
import org.com.recipes.user.entity.id.UserId;
import org.com.recipes.user.service.UserService;
import org.com.recipes.user.service.AvatarService;

@Path("/users")
@RolesAllowed("ADMIN")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private final UserService userService;
    private final AvatarService avatarService;

    @Inject
    public UserController(UserService userService, AvatarService avatarService) {
        this.userService = userService;
        this.avatarService = avatarService;
    }

    @GET
    public CollectionRepresentation<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    public UserDto getUser(@PathParam("id") String id) {
        return userService.getUser(UserId.of(id));
    }

    @GET
    @Path("/{id}/avatar")
    @RolesAllowed({"USER", "ADMIN"})
    @Produces({"image/png", "image/jpeg"})
    public Response getAvatar(@PathParam("id") String id) {
        byte[] image = avatarService.findAvatar(UserId.of(id));
        return Response.ok(image, avatarService.getImageContentType(image)).build();
    }

    @POST
    @PermitAll
    public Response registerUser(RegisterUserDto dto, @Context UriInfo uriInfo) {
        UserDto created = userService.registerUser(dto);
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
