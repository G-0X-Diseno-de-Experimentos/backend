package com.textilflow.platform.iam.infrastructure.tokens;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

/**
 * JWT implementation of token service
 */
@Service
public class JwtTokenService implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);
    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_BEGIN_INDEX = 7;
    private static final int MINIMUM_SECRET_BYTES = 32;
    private static final String DEVELOPMENT_FALLBACK_SECRET =
            "WriteHereYourSecretStringForTokenSigningCredentials";

    private final SecretKey signingKey;
    private final long expirationDays;

    public JwtTokenService(
            @Value("${authorization.jwt.secret:}") String configuredSecret,
            @Value("${authorization.jwt.expiration.days}") long expirationDays,
            @Value("${spring.profiles.active:}") String activeProfiles) {
        this.signingKey = Keys.hmacShaKeyFor(resolveSecret(configuredSecret, activeProfiles));
        this.expirationDays = expirationDays;
    }

    private byte[] resolveSecret(String configuredSecret, String activeProfiles) {
        String normalizedSecret = configuredSecret == null ? "" : configuredSecret.trim();
        if (normalizedSecret.isBlank()) {
            if (isProductionProfileActive(activeProfiles)) {
                throw new IllegalStateException(
                        "JWT secret is blank. Set JWT_SECRET or authorization.jwt.secret with at least 32 bytes.");
            }
            logger.warn("JWT secret is blank. Using development fallback secret.");
            normalizedSecret = DEVELOPMENT_FALLBACK_SECRET;
        }

        byte[] keyBytes = normalizedSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < MINIMUM_SECRET_BYTES) {
            throw new IllegalStateException(
                    "JWT secret must be at least " + MINIMUM_SECRET_BYTES
                            + " bytes. Update JWT_SECRET or authorization.jwt.secret.");
        }
        return keyBytes;
    }

    private boolean isProductionProfileActive(String activeProfiles) {
        String profiles = activeProfiles == null ? "" : activeProfiles;
        return Arrays.stream(profiles.split(","))
                .map(String::trim)
                .filter(profile -> !profile.isEmpty())
                .anyMatch(profile -> profile.equalsIgnoreCase("production"));
    }

    @Override
    public String generateToken(String email) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationDays, ChronoUnit.HOURS)))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            logger.error("Error extracting email from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public String getBearerTokenFrom(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_PARAMETER_NAME);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_BEGIN_INDEX);
        }
        return null;
    }

    @Override
    public String generateResetToken(String email, int expirationMinutes) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject("password-reset")
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public Claims validateResetToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            logger.error("Error validating reset token: {}", e.getMessage());
            throw new RuntimeException("Invalid reset token");
        }
    }
}
