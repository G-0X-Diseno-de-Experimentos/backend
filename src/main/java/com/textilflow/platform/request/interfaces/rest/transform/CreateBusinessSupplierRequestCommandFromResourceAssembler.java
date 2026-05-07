package com.textilflow.platform.request.interfaces.rest.transform;

import com.textilflow.platform.request.domain.model.commands.CreateBusinessSupplierRequestCommand;
import com.textilflow.platform.request.interfaces.rest.resources.CreateBusinessSupplierRequestResource;

public class CreateBusinessSupplierRequestCommandFromResourceAssembler {

    public static CreateBusinessSupplierRequestCommand toCommandFromResource(CreateBusinessSupplierRequestResource resource) {
        return new CreateBusinessSupplierRequestCommand(
                resource.businessmanId(),
                resource.supplierId(),
                resource.message(),
                resource.batchType(),
                resource.color(),
                resource.quantity(),
                resource.address()
        );
    }
}
