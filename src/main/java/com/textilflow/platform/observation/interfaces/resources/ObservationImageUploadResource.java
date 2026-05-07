package com.textilflow.platform.observation.interfaces.resources;

/**
 * Resource for observation image upload response
 */
public record ObservationImageUploadResource(
        Long observationId,
        String imageUrl,
        String message
) {}
