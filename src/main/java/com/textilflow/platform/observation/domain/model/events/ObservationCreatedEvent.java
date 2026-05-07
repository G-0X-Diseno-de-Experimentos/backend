package com.textilflow.platform.observation.domain.model.events;
/**
 * Event fired when a observation is created
 */

public record ObservationCreatedEvent(
        Long batchId,
        String batchCode,
        Long businessmanId,
        Long supplierId,
        String reason
) {
}
