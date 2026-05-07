package com.textilflow.platform.shared.application.services;

/**
 * Email Service
 * Interface for sending emails
 */
public interface EmailService {

    /**
     * Send welcome email to new user
     * @param toEmail Recipient email address
     * @param userName User's name
     * @param userRole User's role (businessman/supplier)
     */
    void sendWelcomeEmail(String toEmail, String userName, String userRole);

    /**
     * Send password reset email
     * @param toEmail Recipient email address
     * @param userName User's name
     * @param resetLink Password reset link
     */
    void sendPasswordResetEmail(String toEmail, String userName, String resetLink);

}