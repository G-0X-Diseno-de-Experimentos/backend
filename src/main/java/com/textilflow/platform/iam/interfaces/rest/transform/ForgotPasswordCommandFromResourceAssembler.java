package com.textilflow.platform.iam.interfaces.rest.transform;

import com.textilflow.platform.iam.domain.model.commands.ForgotPasswordCommand;
import com.textilflow.platform.iam.interfaces.rest.resources.ForgotPasswordResource;

public class ForgotPasswordCommandFromResourceAssembler {
    public static ForgotPasswordCommand toCommandFromResource(ForgotPasswordResource resource) {
        return new ForgotPasswordCommand(resource.email());
    }
}