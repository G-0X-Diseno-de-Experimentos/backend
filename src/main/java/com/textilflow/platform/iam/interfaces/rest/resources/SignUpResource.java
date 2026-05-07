package com.textilflow.platform.iam.interfaces.rest.resources;

/**
 * Sign up resource for user registration
 */
public record SignUpResource(
        String name,
        String email,
        String password,
        String country,
        String city,
        String address,
        String phone,
        String role
) {
}