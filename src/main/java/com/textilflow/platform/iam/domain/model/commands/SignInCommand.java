package com.textilflow.platform.iam.domain.model.commands;

/**
 * Sign in command for user authentication
 */
public record SignInCommand(
        String email,
        String password
) {
}