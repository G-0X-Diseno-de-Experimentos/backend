package com.textilflow.platform.profiles.domain.model.commands;

/**
 * Update businessman command
 */
public record UpdateBusinessmanCommand(
        Long userId,
        String companyName,
        String ruc,
        String businessType,
        String description,
        String website,
        // User data
        String name,
        String email,
        String country,
        String city,
        String address,
        String phone
) {
}