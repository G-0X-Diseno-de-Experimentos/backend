package com.textilflow.platform.observation.domain.model.commands;

public record UpdateObservationCommand(
        Long observationId,
        String reason,
        String imageUrl,
        String status
) {}
