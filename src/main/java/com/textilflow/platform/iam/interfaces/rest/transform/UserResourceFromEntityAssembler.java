package com.textilflow.platform.iam.interfaces.rest.transform;

import com.textilflow.platform.iam.domain.model.aggregates.User;
import com.textilflow.platform.iam.interfaces.rest.resources.UserResource;

/**
 * Assembler to convert User entity to UserResource
 */
public class UserResourceFromEntityAssembler {

    public static UserResource toResourceFromEntity(User entity) {
        return new UserResource(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRoleName(),
                entity.getCountry(),
                entity.getCity(),
                entity.getAddress(),
                entity.getPhone()
        );
    }
}
