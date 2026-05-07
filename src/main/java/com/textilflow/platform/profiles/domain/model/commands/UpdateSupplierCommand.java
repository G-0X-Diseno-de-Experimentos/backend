package com.textilflow.platform.profiles.domain.model.commands;

/**
 * Update supplier command
 */
public record UpdateSupplierCommand(
        Long userId,
        String companyName,
        String ruc,
        String specialization,
        String description,
        String certifications,
        // User data
        String name,
        String email,
        String country,
        String city,
        String address,
        String phone
) {
}