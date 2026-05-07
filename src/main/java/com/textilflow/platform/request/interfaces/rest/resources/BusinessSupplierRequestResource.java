package com.textilflow.platform.request.interfaces.rest.resources;

import com.textilflow.platform.request.domain.model.valueobjects.RequestStatus;

import java.time.LocalDateTime;

public record BusinessSupplierRequestResource(
        Long id,
        Long businessmanId,
        Long supplierId,
        RequestStatus status,
        String message,
        String batchType,
        String color,
        Integer quantity,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
