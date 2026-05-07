package com.textilflow.platform.iam.interfaces.rest.resources;

/**
 * Resource for reset password request
 */
public record ResetPasswordResource(String token, String newPassword) {
}