package com.textilflow.platform.iam.domain.model.commands;

import com.textilflow.platform.iam.domain.model.valueobjects.Roles;

/**
 * Update user role command
 */
public record UpdateUserRoleCommand(
        Long userId,
        Roles newRole
) {
}