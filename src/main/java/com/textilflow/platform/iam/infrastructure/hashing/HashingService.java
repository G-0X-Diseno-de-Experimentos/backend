package com.textilflow.platform.iam.infrastructure.hashing;

/**
 * Hashing service interface for password encoding and verification
 */
public interface HashingService {

    /**
     * Encode a raw password
     * @param rawPassword the raw password
     * @return the encoded password
     */
    String encode(CharSequence rawPassword);

    /**
     * Check if raw password matches encoded password
     * @param rawPassword the raw password
     * @param encodedPassword the encoded password
     * @return true if passwords match
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}