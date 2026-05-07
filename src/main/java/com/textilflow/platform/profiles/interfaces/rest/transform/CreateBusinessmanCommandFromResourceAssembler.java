package com.textilflow.platform.profiles.interfaces.rest.transform;

import com.textilflow.platform.profiles.domain.model.commands.CreateBusinessmanCommand;
import com.textilflow.platform.profiles.interfaces.rest.resources.CreateBusinessmanResource;

/**
 * Assembler to convert CreateBusinessmanResource to CreateBusinessmanCommand
 */
public class CreateBusinessmanCommandFromResourceAssembler {

    public static CreateBusinessmanCommand toCommandFromResource(Long userId, CreateBusinessmanResource resource) {
        return new CreateBusinessmanCommand(
                userId,
                resource.companyName(),
                resource.ruc(),
                resource.businessType(),
                resource.description(),
                resource.website()
        );
    }
}