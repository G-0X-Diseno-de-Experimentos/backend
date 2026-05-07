package com.textilflow.platform.request.interfaces.rest.resources;

public record CreateBusinessSupplierRequestResource(
        Long businessmanId,
        Long supplierId,
        String message,
        String batchType,
        String color,
        Integer quantity,
        String address
) {}
