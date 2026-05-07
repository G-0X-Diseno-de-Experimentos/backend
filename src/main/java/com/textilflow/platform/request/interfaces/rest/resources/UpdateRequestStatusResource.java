package com.textilflow.platform.request.interfaces.rest.resources;

import com.textilflow.platform.request.domain.model.valueobjects.RequestStatus;

public record UpdateRequestStatusResource(
        RequestStatus status,
        String message
) {}
