package com.textilflow.platform.iam.domain.model.commands;

import com.textilflow.platform.iam.domain.model.valueobjects.Roles;

/**
 * Sign up command for user registration
 * Contains all user information for complete registration
 */
public record SignUpCommand(
        String name,
        String email,
        String password,
        String country,
        String city,
        String address,
        String phone,
        Roles role
) {
}