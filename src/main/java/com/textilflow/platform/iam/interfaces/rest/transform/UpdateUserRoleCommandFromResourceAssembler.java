package com.textilflow.platform.iam.interfaces.rest.transform;

import com.textilflow.platform.iam.domain.model.commands.UpdateUserRoleCommand;
import com.textilflow.platform.iam.domain.model.valueobjects.Roles;
import com.textilflow.platform.iam.interfaces.rest.resources.UpdateUserRoleResource;

/**
 * Assembler to convert UpdateUserRoleResource to UpdateUserRoleCommand
 */
public class UpdateUserRoleCommandFromResourceAssembler {

    public static UpdateUserRoleCommand toCommandFromResource(Long userId, UpdateUserRoleResource resource) {
        var role = parseRole(resource.role());
        return new UpdateUserRoleCommand(userId, role);
    }

    private static Roles parseRole(String roleString) {
        return switch (roleString.toUpperCase()) {
            case "BUSINESSMAN" -> Roles.BUSINESSMAN;
            case "SUPPLIER" -> Roles.SUPPLIER;
            case "PENDING" -> Roles.PENDING;
            default -> throw new IllegalArgumentException("Invalid role: " + roleString);
        };
    }
}