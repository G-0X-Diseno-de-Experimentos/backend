package com.textilflow.platform.profiles.interfaces.rest.resources;

/**
 * Create supplier resource
 */
public record CreateSupplierResource(
        String companyName,
        String ruc,
        String specialization,
        String description,
        String certifications
) {
}