package com.textilflow.platform.profiles.interfaces.rest.resources;

/**
 * Update supplier resource
 */
public record UpdateSupplierResource(
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