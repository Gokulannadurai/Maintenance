package com.ideas2it.maintenanceservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;

/**
 * JWT token provider for generating and validating JWTs.
 */
@Component
public class JwtTokenProvider {

    private static final long JWT_EXPIRATION_MS = 86400000; // 1 day
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret:MySecretKeyForJWT1234567890}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generate a JWT token for a user.
     * @param username the username
     * @param roles the user's roles
     * @return the JWT token
     */
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Get username from JWT token.
     * @param token the JWT token
     * @return the username
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get roles from JWT token.
     * @param token the JWT token
     * @return the roles
     */
    public Set<String> getRoles(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims.get("roles", Set.class);
    }

    /**
     * Validate a JWT token.
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
} 