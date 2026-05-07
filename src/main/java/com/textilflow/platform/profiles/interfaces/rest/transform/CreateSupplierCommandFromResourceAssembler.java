package com.textilflow.platform.profiles.interfaces.rest.transform;

import com.textilflow.platform.profiles.domain.model.commands.CreateSupplierCommand;
import com.textilflow.platform.profiles.interfaces.rest.resources.CreateSupplierResource;

/**
 * Assembler to convert CreateSupplierResource to CreateSupplierCommand
 */
public class CreateSupplierCommandFromResourceAssembler {

    public static CreateSupplierCommand toCommandFromResource(Long userId, CreateSupplierResource resource) {
        return new CreateSupplierCommand(
                userId,
                resource.companyName(),
                resource.ruc(),
                resource.specialization(),
                resource.description(),
                resource.certifications()
        );
    }
}