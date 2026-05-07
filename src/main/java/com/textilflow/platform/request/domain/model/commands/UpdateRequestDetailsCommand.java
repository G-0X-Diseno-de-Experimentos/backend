package com.textilflow.platform.request.domain.model.commands;

public record UpdateRequestDetailsCommand(
        Long requestId,
        String message,
        String batchType,
        String color,
        Integer quantity,
        String address
) {}
