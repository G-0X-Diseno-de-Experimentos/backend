package com.textilflow.platform.observation.interfaces.resources;

import com.textilflow.platform.observation.domain.model.aggregates.Observation;

import java.util.Date;

public record ObservationResource(
        Long id,
        Long batchId,
        String batchCode,
        Long businessmanId,
        Long supplierId,
        String reason,
        String imageUrl,
        String status,
        Date createdAt
) {
    public static ObservationResource fromEntity(Observation observation) {
        return new ObservationResource(
                observation.getId(),
                observation.getBatchId(),
                observation.getBatchCodeValue(),
                observation.getBusinessmanId(),
                observation.getSupplierId(),
                observation.getReason(),
                observation.getImageUrlValue(),
                observation.getStatusValue(),
                observation.getCreatedAt()
        );
    }
}
