package com.textilflow.platform.profiles.interfaces.rest.resources;

/**
 * Supplier resource for responses
 */
public record SupplierResource(
        Long userId,
        String companyName,
        String ruc,
        String specialization,
        String description,
        String certifications,
        String logoUrl,
        // User data
        String name,
        String email,
        String country,
        String city,
        String address,
        String phone
) {
}