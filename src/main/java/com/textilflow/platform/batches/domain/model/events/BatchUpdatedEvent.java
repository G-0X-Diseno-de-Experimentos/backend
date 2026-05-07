package com.textilflow.platform.batches.domain.model.events;

import com.textilflow.platform.batches.domain.model.valueobjects.BatchStatus;

import java.time.LocalDate;

public record BatchUpdatedEvent(
        Long batchId,
        String code,
        String client,
        Long businessmanId,
        Long supplierId,
        String fabricType,
        String color,
        Integer quantity,
        Double price,
        String observations,
        String address,
        LocalDate date,
        BatchStatus status,
        String imageUrl
) {}
