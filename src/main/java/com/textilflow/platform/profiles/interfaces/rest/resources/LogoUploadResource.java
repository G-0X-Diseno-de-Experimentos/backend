package com.textilflow.platform.profiles.interfaces.rest.resources;

/**
 * Logo upload response resource
 */
public record LogoUploadResource(
        Long userId,
        String logoUrl,
        String message
) {
}