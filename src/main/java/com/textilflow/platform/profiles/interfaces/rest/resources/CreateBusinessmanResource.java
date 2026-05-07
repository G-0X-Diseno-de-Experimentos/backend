package com.textilflow.platform.profiles.interfaces.rest.resources;

/**
 * Create businessman resource
 */
public record CreateBusinessmanResource(
        String companyName,
        String ruc,
        String businessType,
        String description,
        String website
) {
}