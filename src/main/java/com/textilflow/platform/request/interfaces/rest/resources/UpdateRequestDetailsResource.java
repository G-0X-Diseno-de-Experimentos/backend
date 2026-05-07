package com.textilflow.platform.request.interfaces.rest.resources;

public record UpdateRequestDetailsResource(
        String message,
        String batchType,
        String color,
        Integer quantity,
        String address
) {}
