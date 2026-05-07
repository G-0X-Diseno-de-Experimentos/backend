package com.textilflow.platform.iam.interfaces.rest.resources;

/**
 * User resource for responses
 */
public record UserResource(
        Long id,
        String name,
        String email,
        String role,
        String country,
        String city,
        String address,
        String phone
) {
}