package com.textilflow.platform.profiles.interfaces.rest.transform;

import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import com.textilflow.platform.profiles.interfaces.rest.resources.SupplierResource;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;

/**
 * Assembler to convert Supplier entity to SupplierResource
 */
public class SupplierResourceFromEntityAssembler {

    public static SupplierResource toResourceFromEntity(Supplier entity, ExternalIamService iamService) {
        // Get user data from IAM context
        var userData = iamService.getUserData(entity.getUserId());

        return new SupplierResource(
                entity.getUserId(),
                entity.getCompanyNameValue(),
                entity.getRucValue(),
                entity.getSpecializationValue(),
                entity.getDescription(),
                entity.getCertifications(),
                entity.getLogoUrlValue(),
                // User data from IAM
                userData.name(),
                userData.email(),
                userData.country(),
                userData.city(),
                userData.address(),
                userData.phone()
        );
    }
}