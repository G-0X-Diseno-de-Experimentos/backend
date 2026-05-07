package com.textilflow.platform.iam.interfaces.rest.resources;

/**
 * Sign in resource for user authentication
 */
public record SignInResource(
        String email,
        String password
) {
}