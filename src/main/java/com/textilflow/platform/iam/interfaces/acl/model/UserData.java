package com.textilflow.platform.iam.interfaces.acl.model;

/**
 * User data transfer record for ACL communication
 */
public record UserData(
        String name,
        String email,
        String country,
        String city,
        String address,
        String phone
) {
}