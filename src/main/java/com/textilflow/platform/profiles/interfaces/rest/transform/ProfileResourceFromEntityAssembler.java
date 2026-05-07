package com.textilflow.platform.profiles.interfaces.rest.transform;

import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import com.textilflow.platform.profiles.interfaces.rest.resources.ProfileResource;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;

/**
 * Assembler to convert profile entities to ProfileResource
 */
public class ProfileResourceFromEntityAssembler {

    public static ProfileResource toResourceFromEntities(String userRole, ExternalIamService iamService,
                                                         Long userId, Businessman businessman, Supplier supplier) {
        // Get user data from IAM context
        var userData = iamService.getUserData(userId);

        var businessmanResource = businessman != null ?
                BusinessmanResourceFromEntityAssembler.toResourceFromEntity(businessman, iamService) : null;

        var supplierResource = supplier != null ?
                SupplierResourceFromEntityAssembler.toResourceFromEntity(supplier, iamService) : null;

        return new ProfileResource(
                userId,
                userRole,
                userData.name(),
                userData.email(),
                userData.country(),
                userData.city(),
                userData.address(),
                userData.phone(),
                businessmanResource,
                supplierResource
        );
    }
}