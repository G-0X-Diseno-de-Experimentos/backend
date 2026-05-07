package com.textilflow.platform.request.domain.model.commands;

import com.textilflow.platform.request.domain.model.valueobjects.RequestStatus;

public record UpdateRequestStatusCommand(
        Long requestId,
        RequestStatus status,
        String message
) {}
