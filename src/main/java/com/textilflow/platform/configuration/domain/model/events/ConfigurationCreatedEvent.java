package com.textilflow.platform.configuration.domain.model.events;

/**
 * Configuration created event
 */
public record ConfigurationCreatedEvent(
        Long configurationId,
        Long userId,
        String language,
        String viewMode,
        String subscriptionPlan,
        String subscriptionStatus
) {
}