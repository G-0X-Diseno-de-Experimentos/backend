package com.textilflow.platform.iam.application.services;

import com.textilflow.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.textilflow.platform.iam.infrastructure.tokens.TokenService;
import com.textilflow.platform.shared.application.services.EmailService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final String frontendUrl;

    public PasswordResetService(EmailService emailService,
                                UserRepository userRepository,
                                TokenService tokenService,
                                @Value("${app.frontend.url:https://textilflow.web.app}") String frontendUrl) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.frontendUrl = frontendUrl;
    }

    public boolean sendPasswordResetEmail(String email) {
        var userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            // Por seguridad, siempre retornamos true
            return true;
        }

        var user = userOptional.get();

        // Generar token con expiración de 60 minutos
        String resetToken = tokenService.generateResetToken(user.getEmail(), 60);

        // Enviar email
        emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getName(),
                frontendUrl + "/reset-password?token=" + resetToken
        );

        return true;
    }

    public String validateResetToken(String token) {
        try {
            Claims claims = tokenService.validateResetToken(token);
            return claims.get("email", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}