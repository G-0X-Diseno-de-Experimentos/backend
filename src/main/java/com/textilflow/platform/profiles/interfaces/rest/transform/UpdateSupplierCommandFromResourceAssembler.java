package com.textilflow.platform.profiles.interfaces.rest.transform;

import com.textilflow.platform.profiles.domain.model.commands.UpdateSupplierCommand;
import com.textilflow.platform.profiles.interfaces.rest.resources.UpdateSupplierResource;

/**
 * Assembler to convert UpdateSupplierResource to UpdateSupplierCommand
 */
public class UpdateSupplierCommandFromResourceAssembler {

    public static UpdateSupplierCommand toCommandFromResource(Long userId, UpdateSupplierResource resource) {
        return new UpdateSupplierCommand(
                userId,
                resource.companyName(),
                resource.ruc(),
                resource.specialization(),
                resource.description(),
                resource.certifications(),
                // User data
                resource.name(),
                resource.email(),
                resource.country(),
                resource.city(),
                resource.address(),
                resource.phone()
        );
    }
}