package com.textilflow.platform.iam.interfaces.acl;

import com.textilflow.platform.iam.interfaces.acl.model.UserData;

/**
 * Facade for IAM context to provide services to other bounded contexts
 */
public interface IamContextFacade {

    /**
     * Get user ID by email for other contexts
     * @param email the user email
     * @return the user ID
     */
    Long getUserIdByEmail(String email);

    /**
     * Check if user exists
     * @param userId the user ID
     * @return true if user exists
     */
    boolean userExists(Long userId);

    /**
     * Get user role
     * @param userId the user ID
     * @return the user role
     */
    String getUserRole(Long userId);

    /**
     * Get user data
     * @param userId the user ID
     * @return the user data
     */
    UserData getUserData(Long userId);

    /**
     * Update user data
     * @param userId the user ID
     * @param name the user name
     * @param email the user email
     * @param country the user country
     * @param city the user city
     * @param address the user address
     * @param phone the user phone
     */
    void updateUserData(Long userId, String name, String email,
                        String country, String city, String address, String phone);
}