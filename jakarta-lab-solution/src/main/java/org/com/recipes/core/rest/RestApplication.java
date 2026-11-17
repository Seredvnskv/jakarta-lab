package org.com.recipes.core.rest;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@DeclareRoles({"USER", "ADMIN"})
public class RestApplication extends Application {
}
