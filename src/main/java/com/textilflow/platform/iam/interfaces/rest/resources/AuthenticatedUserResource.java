package com.textilflow.platform.iam.interfaces.rest.resources;

/**
 * Authenticated user resource with token
 */
public record AuthenticatedUserResource(
        Long id,
        String name,
        String email,
        String role,
        String token
) {
}