package com.textilflow.platform.profiles.domain.model.events;

/**
 * Event fired when a supplier is created
 */
public record SupplierCreatedEvent(
        Long userId,
        String companyName,
        String ruc,
        String specialization
) {
}