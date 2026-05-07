package com.textilflow.platform.request.domain.model.commands;

public record CreateBusinessSupplierRequestCommand(
        Long businessmanId,
        Long supplierId,
        String message,
        String batchType,
        String color,
        Integer quantity,
        String address
) {}
