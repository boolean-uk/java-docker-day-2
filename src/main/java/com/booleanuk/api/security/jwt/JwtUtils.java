package com.booleanuk.api.security.jwt;

import com.booleanuk.api.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
// Marks the class as a Spring component for dependency injection.
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${booleanuk.app.jwtSecret}")
    // Secret key used for signing the JWT tokens, injected from application properties.
    private String jwtSecret;

    @Value("${booleanuk.app.jwtExpirationMs}")
    // Token expiration time in milliseconds, injected from application properties.
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        /*
        Generates a JWT token for the authenticated user.

        Retrieves the user details (UserDetailsImpl) from the Authentication object.

        Builds a JWT token using:
            - Subject: Username of the user.
            - Issued At: Current date and time.
            - Expiration: Current time plus the expiration time defined in jwtExpirationMs.
            - Signature: Signs the token using the secret key.
            - Returns the compact JWT token as a string.
         */
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + this.jwtExpirationMs))
                .signWith(this.key())
                .compact();
    }

    private SecretKey key() {
        /*
        Generates a SecretKey using the secret key string decoded from Base64.
        Used for signing and verifying JWT tokens.
        */
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        /*
        Parses the JWT token to extract the username (subject).

        Process:
            - Builds a JWT parser with the signing key.
            - Parses the token to get the claims.
            - Retrieves the subject (username) from the claims.
         */
        return Jwts.parser().verifyWith(this.key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        /*
        Validates the JWT token.

        Process:
            - Tries to parse the token using the signing key.
            - If parsing is successful, the token is valid.
            - Catches exceptions related to invalid tokens and logs the error.
            - Returns true if valid, false otherwise.
         */
        try {
            Jwts.parser().verifyWith(this.key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}