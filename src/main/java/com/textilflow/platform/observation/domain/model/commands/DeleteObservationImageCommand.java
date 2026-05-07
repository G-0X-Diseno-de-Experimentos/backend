package com.textilflow.platform.observation.domain.model.commands;

/**
 * Command to delete observation image
 */
public record DeleteObservationImageCommand(
        Long observationId
) {}
