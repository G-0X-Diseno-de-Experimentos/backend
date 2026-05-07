package com.textilflow.platform.observation.domain.model.commands;

public record CreateObservationCommand(
        Long batchId,
        String batchCode,
        Long businessmanId,
        Long supplierId,
        String reason,
        String imageUrl,
        String status
) {}