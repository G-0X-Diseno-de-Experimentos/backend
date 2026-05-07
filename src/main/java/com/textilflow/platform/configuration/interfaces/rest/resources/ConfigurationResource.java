package com.textilflow.platform.configuration.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * Configuration resource for API responses
 */
public record ConfigurationResource(
        Long id,
        Long userId,
        String language,
        String viewMode,
        String subscriptionPlan,
        String subscriptionStatus,
        LocalDateTime subscriptionStartDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}