package org.com.recipes.core.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedAccessExceptionMapper implements ExceptionMapper<UnauthorizedAccessException> {
    @Override
    public Response toResponse(UnauthorizedAccessException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
