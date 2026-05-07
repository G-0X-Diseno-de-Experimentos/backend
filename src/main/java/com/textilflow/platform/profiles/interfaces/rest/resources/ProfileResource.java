package com.textilflow.platform.profiles.interfaces.rest.resources;

/**
 * Complete profile resource
 */
public record ProfileResource(
        Long userId,
        String userRole,
        // User data
        String name,
        String email,
        String country,
        String city,
        String address,
        String phone,
        // Business data (nullable based on role)
        BusinessmanResource businessman,
        SupplierResource supplier
) {
}