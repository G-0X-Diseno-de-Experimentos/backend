package com.textilflow.platform.observation.interfaces.resources;

public record UpdateObservationResource(
        String reason,
        String imageUrl,
        String status
) {}
