package com.textilflow.platform.profiles.interfaces.rest.resources;

/**
 * Update businessman resource
 */
public record UpdateBusinessmanResource(
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