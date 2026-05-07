package com.textilflow.platform.iam.domain.model.commands;

/**
 * Command for forgot password request
 */
public record ForgotPasswordCommand(String email) {
}