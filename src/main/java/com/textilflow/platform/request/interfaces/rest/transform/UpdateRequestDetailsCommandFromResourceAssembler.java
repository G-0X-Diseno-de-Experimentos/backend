package com.textilflow.platform.request.interfaces.rest.transform;

import com.textilflow.platform.request.domain.model.commands.UpdateRequestDetailsCommand;
import com.textilflow.platform.request.interfaces.rest.resources.UpdateRequestDetailsResource;

public class UpdateRequestDetailsCommandFromResourceAssembler {

    public static UpdateRequestDetailsCommand toCommandFromResource(Long requestId, UpdateRequestDetailsResource resource) {
        return new UpdateRequestDetailsCommand(
                requestId,
                resource.message(),
                resource.batchType(),
                resource.color(),
                resource.quantity(),
                resource.address()
        );
    }
}
