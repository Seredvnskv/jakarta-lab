package org.com.recipes.core.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AccessForbiddenExceptionMapper implements ExceptionMapper<AccessForbiddenException> {
    @Override
    public Response toResponse(AccessForbiddenException exception) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
