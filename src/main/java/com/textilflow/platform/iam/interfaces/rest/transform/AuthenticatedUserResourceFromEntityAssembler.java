package com.textilflow.platform.iam.interfaces.rest.transform;

import com.textilflow.platform.iam.domain.model.aggregates.User;
import com.textilflow.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

/**
 * Assembler to convert User entity and token to AuthenticatedUserResource
 */
public class AuthenticatedUserResourceFromEntityAssembler {

    public static AuthenticatedUserResource toResourceFromEntity(User entity, String token) {
        return new AuthenticatedUserResource(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRoleName(),
                token
        );
    }
}