// BusinessmanResource.java
package com.textilflow.platform.profiles.interfaces.rest.resources;

/**
 * Businessman resource for responses
 */
public record BusinessmanResource(
        Long userId,
        String companyName,
        String ruc,
        String businessType,
        String description,
        String website,
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