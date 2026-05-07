package com.textilflow.platform.iam.application.outbound;

/**
 * External service interface for communication with Users context
 */
public interface ExternalUsersService {

    /**
     * Notify Users context about user registration
     * @param userId the user ID
     * @param email user email
     * @param name user name
     * @param country user country
     * @param city user city
     * @param address user address
     * @param phone user phone
     */
    void notifyUserRegistered(Long userId, String email, String name,
                              String country, String city, String address, String phone);

    /**
     * Notify Users context about role update
     * @param userId the user ID
     * @param oldRole previous role
     * @param newRole new role
     */
    void notifyUserRoleUpdated(Long userId, String oldRole, String newRole);
}