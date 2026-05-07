package com.textilflow.platform.iam.domain.model.commands;

/**
 * Update user data command
 */
public record UpdateUserDataCommand(
        Long userId,
        String name,
        String email,
        String country,
        String city,
        String address,
        String phone
) {
}