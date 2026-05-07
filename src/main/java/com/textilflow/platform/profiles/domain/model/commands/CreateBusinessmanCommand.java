package com.textilflow.platform.profiles.domain.model.commands;

/**
 * Create businessman command
 */
public record CreateBusinessmanCommand(
        Long userId,
        String companyName,
        String ruc,
        String businessType,
        String description,
        String website
) {
}