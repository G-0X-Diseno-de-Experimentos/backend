package com.textilflow.platform.observation.interfaces.resources;

public record CreateObservationResource(
        Long batchId,
        String batchCode,
        Long businessmanId,
        Long supplierId,
        String reason,
        String imageUrl,
        String status
) {}
