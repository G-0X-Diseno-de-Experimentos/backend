package com.textilflow.platform.request.interfaces.rest.transform;

import com.textilflow.platform.request.domain.model.commands.UpdateRequestStatusCommand;
import com.textilflow.platform.request.interfaces.rest.resources.UpdateRequestStatusResource;

public class UpdateRequestStatusCommandFromResourceAssembler {

    public static UpdateRequestStatusCommand toCommandFromResource(Long requestId, UpdateRequestStatusResource resource) {
        return new UpdateRequestStatusCommand(
                requestId,
                resource.status(),
                resource.message()
        );
    }
}
