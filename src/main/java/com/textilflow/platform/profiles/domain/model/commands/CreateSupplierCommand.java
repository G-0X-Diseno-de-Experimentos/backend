package com.textilflow.platform.profiles.domain.model.commands;

/**
 * Create supplier command
 */
public record CreateSupplierCommand(
        Long userId,
        String companyName,
        String ruc,
        String specialization,
        String description,
        String certifications
) {
}