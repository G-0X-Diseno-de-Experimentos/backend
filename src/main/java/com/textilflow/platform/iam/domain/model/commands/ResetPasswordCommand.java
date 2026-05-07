package com.textilflow.platform.iam.domain.model.commands;

/**
 * Command for reset password request
 */
public record ResetPasswordCommand(String token, String newPassword) {
}