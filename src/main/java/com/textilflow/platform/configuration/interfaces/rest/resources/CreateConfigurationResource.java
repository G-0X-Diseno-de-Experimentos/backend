package com.textilflow.platform.configuration.interfaces.rest.resources;

/**
 * Resource for creating configuration
 */
public record CreateConfigurationResource(
        Long userId,
        String language,
        String viewMode,
        String subscriptionPlan
) {
}