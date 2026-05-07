package com.textilflow.platform.iam.application.services;

import com.textilflow.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.textilflow.platform.shared.application.services.EmailService;
import org.springframework.stereotype.Service;

/**
 * Service for sending welcome and notification emails
 */
@Service
public class WelcomeEmailService {

    private final EmailService emailService;
    private final UserRepository userRepository;

    public WelcomeEmailService(EmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public void sendWelcomeEmail(String email, String name) {
        emailService.sendWelcomeEmail(email, name, "pending");
    }

    public void sendRoleUpdateNotification(Long userId, String oldRole, String newRole) {
        var user = userRepository.findById(userId);
        if (user.isPresent()) {
            // ✅ USAR LOS MÉTODOS CORRECTOS DE TU USER ENTITY:
            emailService.sendWelcomeEmail(
                    user.get().getEmail(),     // ✅ getEmail() en lugar de getEmailAddress()
                    user.get().getName(),      // ✅ getName() en lugar de getEmailAddress()
                    newRole.toLowerCase()
            );
        }
    }
}