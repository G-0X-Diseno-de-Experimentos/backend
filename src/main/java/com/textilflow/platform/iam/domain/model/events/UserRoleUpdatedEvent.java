package com.textilflow.platform.iam.domain.model.events;

import com.textilflow.platform.iam.domain.model.valueobjects.Roles;

/**
 * Domain event fired when a user role is updated
 * This event will be consumed by PROFILES context to activate specific profiles
 */
public record UserRoleUpdatedEvent(
        Long userId,
        Roles oldRole,
        Roles newRole
) {
}