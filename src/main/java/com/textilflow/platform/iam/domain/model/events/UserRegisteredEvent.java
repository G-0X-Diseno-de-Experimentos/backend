package com.textilflow.platform.iam.domain.model.events;

/**
 * Domain event fired when a user is registered
 * This event will be consumed by PROFILES context to create user profile
 */
public record UserRegisteredEvent(
        Long userId,
        String email,
        String name,
        String country,
        String city,
        String address,
        String phone
) {
}