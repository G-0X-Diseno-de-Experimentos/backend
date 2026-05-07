package com.textilflow.platform.profiles.application.internal.outboundservices.acl;

import com.textilflow.platform.iam.interfaces.acl.IamContextFacade;
import com.textilflow.platform.iam.interfaces.acl.model.UserData; // ✅ AGREGAR ESTE IMPORT
import org.springframework.stereotype.Service;

/**
 * External IAM service for communication with IAM context
 */
@Service
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Check if user exists in IAM context
     */
    public boolean userExists(Long userId) {
        return iamContextFacade.userExists(userId);
    }

    /**
     * Get user role from IAM context
     */
    public String getUserRole(Long userId) {
        return iamContextFacade.getUserRole(userId);
    }

    /**
     * Update user data in IAM context
     */
    public void updateUserData(Long userId, String name, String email,
                               String country, String city, String address, String phone) {
        iamContextFacade.updateUserData(userId, name, email, country, city, address, phone);
    }

    // ✅ AGREGAR ESTE MÉTODO:
    /**
     * Get user data from IAM context
     */
    public UserData getUserData(Long userId) {
        return iamContextFacade.getUserData(userId);
    }
}