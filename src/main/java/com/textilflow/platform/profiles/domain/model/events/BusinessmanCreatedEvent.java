package com.textilflow.platform.profiles.domain.model.events;

/**
 * Event fired when a businessman is created
 */
public record BusinessmanCreatedEvent(
        Long userId,
        String companyName,
        String ruc,
        String businessType
) {
}