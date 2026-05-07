package com.textilflow.platform.configuration.application.internal.outboundservices.acl;

import com.textilflow.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

/**
 * External IAM service for configuration context
 */
@Service("configurationExternalIamService")
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Check if user exists
     */
    public boolean userExists(Long userId) {
        return iamContextFacade.userExists(userId);
    }

    /**
     * Get user role
     */
    public String getUserRole(Long userId) {
        return iamContextFacade.getUserRole(userId);
    }
}
