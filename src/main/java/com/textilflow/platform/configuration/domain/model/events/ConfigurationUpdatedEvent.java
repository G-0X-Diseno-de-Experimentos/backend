package com.textilflow.platform.configuration.domain.model.events;

/**
 * Configuration updated event
 */
public record ConfigurationUpdatedEvent(
        Long configurationId,
        Long userId,
        String language,
        String viewMode,
        String subscriptionPlan,
        String subscriptionStatus
) {
}