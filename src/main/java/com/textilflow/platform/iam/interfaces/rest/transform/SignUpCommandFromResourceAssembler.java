package com.textilflow.platform.iam.interfaces.rest.transform;

import com.textilflow.platform.iam.domain.model.commands.SignUpCommand;
import com.textilflow.platform.iam.domain.model.valueobjects.Roles;
import com.textilflow.platform.iam.interfaces.rest.resources.SignUpResource;

/**
 * Assembler to convert SignUpResource to SignUpCommand
 */
public class SignUpCommandFromResourceAssembler {

    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        var role = parseRole(resource.role());
        return new SignUpCommand(
                resource.name(),
                resource.email(),
                resource.password(),
                resource.country(),
                resource.city(),
                resource.address(),
                resource.phone(),
                role
        );
    }

    private static Roles parseRole(String roleString) {
        if (roleString == null || roleString.isEmpty()) {
            return Roles.PENDING;
        }

        return switch (roleString.toUpperCase()) {
            case "BUSINESSMAN" -> Roles.BUSINESSMAN;
            case "SUPPLIER" -> Roles.SUPPLIER;
            default -> Roles.PENDING;
        };
    }
}