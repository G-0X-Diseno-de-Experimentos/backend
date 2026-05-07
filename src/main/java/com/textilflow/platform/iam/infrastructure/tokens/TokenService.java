package com.textilflow.platform.iam.infrastructure.tokens;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Token service interface for JWT operations
 */
public interface TokenService {

    /**
     * Generate JWT token for user
     * @param email user email
     * @return JWT token
     */
    String generateToken(String email);

    /**
     * Extract email from JWT token
     * @param token JWT token
     * @return user email
     */
    String getEmailFromToken(String token);

    /**
     * Validate JWT token
     * @param token JWT token
     * @return true if token is valid
     */
    boolean validateToken(String token);

    /**
     * Extract bearer token from HTTP request
     * @param request HTTP request
     * @return bearer token
     */
    String generateResetToken(String email, int expirationMinutes);
    Claims validateResetToken(String token);
    String getBearerTokenFrom(HttpServletRequest request);
}