package com.textilflow.platform.iam.interfaces.rest.transform;

import com.textilflow.platform.iam.domain.model.commands.SignInCommand;
import com.textilflow.platform.iam.interfaces.rest.resources.SignInResource;

/**
 * Assembler to convert SignInResource to SignInCommand
 */
public class SignInCommandFromResourceAssembler {

    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(
                resource.email(),
                resource.password()
        );
    }
}