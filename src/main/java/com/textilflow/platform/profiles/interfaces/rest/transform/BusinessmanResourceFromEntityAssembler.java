package com.textilflow.platform.profiles.interfaces.rest.transform;

import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import com.textilflow.platform.profiles.interfaces.rest.resources.BusinessmanResource;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;

/**
 * Assembler to convert Businessman entity to BusinessmanResource
 */
public class BusinessmanResourceFromEntityAssembler {

    public static BusinessmanResource toResourceFromEntity(Businessman entity, ExternalIamService iamService) {
        // Get user data from IAM context
        var userData = iamService.getUserData(entity.getUserId());

        return new BusinessmanResource(
                entity.getUserId(),
                entity.getCompanyNameValue(),
                entity.getRucValue(),
                entity.getBusinessTypeValue(),
                entity.getDescription(),
                entity.getWebsite(),
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