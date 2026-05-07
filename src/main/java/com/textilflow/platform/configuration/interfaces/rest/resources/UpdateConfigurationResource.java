package com.textilflow.platform.configuration.interfaces.rest.resources;

/**
 * Resource for updating configuration
 */
public record UpdateConfigurationResource(
        String language,
        String viewMode,
        String subscriptionPlan,
        String subscriptionStatus
){
}