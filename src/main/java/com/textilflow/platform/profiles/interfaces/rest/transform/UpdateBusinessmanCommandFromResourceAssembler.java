package com.textilflow.platform.profiles.interfaces.rest.transform;

import com.textilflow.platform.profiles.domain.model.commands.UpdateBusinessmanCommand;
import com.textilflow.platform.profiles.interfaces.rest.resources.UpdateBusinessmanResource;

/**
 * Assembler to convert UpdateBusinessmanResource to UpdateBusinessmanCommand
 */
public class UpdateBusinessmanCommandFromResourceAssembler {

    public static UpdateBusinessmanCommand toCommandFromResource(Long userId, UpdateBusinessmanResource resource) {
        return new UpdateBusinessmanCommand(
                userId,
                resource.companyName(),
                resource.ruc(),
                resource.businessType(),
                resource.description(),
                resource.website(),
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