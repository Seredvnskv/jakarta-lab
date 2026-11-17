package org.com.recipes.core.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.net.URI;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

@Log
public class JwtAuthMechanism implements HttpAuthenticationMechanism {

    private static final String BEARER = "Bearer";
    private static final String GROUPS_CLAIM = "groups";

    private String issuer;
    private ConfigurableJWTProcessor<SecurityContext> processor;

    @SneakyThrows
    public JwtAuthMechanism(String issuer, String audience, URI jwtSetUri) {
        this.issuer = issuer;
        JWKSource<SecurityContext> keys = JWKSourceBuilder.create(jwtSetUri.toURL()).build();
        processor = new DefaultJWTProcessor<>();
        processor.setJWSKeySelector(new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keys));
        processor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
                audience,
                new JWTClaimsSet.Builder().issuer(issuer).build(),
                Set.of(JWTClaimNames.SUBJECT, JWTClaimNames.EXPIRATION_TIME)
        ));
    }

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) throws AuthenticationException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.regionMatches(true, 0, BEARER + " ", 0, BEARER.length() + 1)) {
            if (context.isProtected()) {
                response.setHeader(HttpHeaders.WWW_AUTHENTICATE, BEARER);
                return context.responseUnauthorized();
            }
            return context.doNothing();
        }
        try {
            JWTClaimsSet claims = processor.process(authorization.substring(BEARER.length() + 1).trim(), null);
            return context.notifyContainerAboutLogin(new CredentialValidationResult(
                    issuer,
                    new CallerPrincipal(claims.getSubject()),
                    null,
                    claims.getSubject(),
                    groups(claims)
            ));
        } catch (KeySourceException ex) {
            throw new AuthenticationException("Unable to retrieve token verification keys", ex);
        } catch (ParseException | BadJOSEException | JOSEException ex) {
            log.log(Level.FINE, "Invalid bearer token", ex);
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, BEARER + " error=\"invalid_token\"");
            return context.responseUnauthorized();
        }
    }

    private static Set<String> groups(JWTClaimsSet claims) throws ParseException {
        List<String> groups = claims.getStringListClaim(GROUPS_CLAIM);
        return groups != null ? new HashSet<>(groups) : Set.of();
    }
}
