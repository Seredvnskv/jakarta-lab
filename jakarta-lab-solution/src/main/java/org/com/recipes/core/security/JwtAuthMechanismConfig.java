package org.com.recipes.core.security;

import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;

@ApplicationScoped
public class JwtAuthMechanismConfig extends JwtAuthMechanism {
    public JwtAuthMechanismConfig() {
        super("http://localhost:9090", "jakarta-lab", URI.create("http://localhost:9090/oauth2/jwks"));
    }
}
