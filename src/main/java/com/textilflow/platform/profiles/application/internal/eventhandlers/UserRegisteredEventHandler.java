package com.textilflow.platform.profiles.application.internal.eventhandlers;

import com.textilflow.platform.iam.application.services.WelcomeEmailService;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import com.textilflow.platform.iam.domain.model.events.UserRegisteredEvent;
import com.textilflow.platform.iam.domain.model.events.UserRoleUpdatedEvent;
import com.textilflow.platform.profiles.domain.model.commands.CreateBusinessmanCommand;
import com.textilflow.platform.profiles.domain.model.commands.CreateSupplierCommand;
import com.textilflow.platform.profiles.domain.services.BusinessmanCommandService;
import com.textilflow.platform.profiles.domain.services.SupplierCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Event handler for IAM user events
 */
@Service
public class UserRegisteredEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredEventHandler.class);
    private final BusinessmanCommandService businessmanCommandService;
    private final SupplierCommandService supplierCommandService;
    private final WelcomeEmailService welcomeEmailService;
    private final ExternalIamService externalIamService;

    public UserRegisteredEventHandler(BusinessmanCommandService businessmanCommandService,
                                      SupplierCommandService supplierCommandService,
                                      WelcomeEmailService welcomeEmailService,
                                      ExternalIamService externalIamService) {
        this.businessmanCommandService = businessmanCommandService;
        this.supplierCommandService = supplierCommandService;
        this.welcomeEmailService = welcomeEmailService;
        this.externalIamService = externalIamService;
    }

    @EventListener
    public void on(UserRegisteredEvent event) {
        logger.info("Handling UserRegisteredEvent for user: {}", event.userId());

        try {
            // Obtener datos del usuario del contexto IAM
            var userData = externalIamService.getUserData(event.userId());

            // Enviar email de bienvenida
            welcomeEmailService.sendWelcomeEmail(
                    userData.email(),
                    userData.name()
            );

            logger.info("Welcome email sent to user: {} ({})", event.userId(), userData.email());
        } catch (Exception e) {
            logger.error("Error sending welcome email to user {}: {}", event.userId(), e.getMessage());
        }

        logger.info("User registered, waiting for role assignment to create specific profile");
    }

    @EventListener
    public void on(UserRoleUpdatedEvent event) {
        logger.info("Handling UserRoleUpdatedEvent for user: {} - Role: {}",
                event.userId(), event.newRole());

        switch (event.newRole()) {
            case BUSINESSMAN -> createBusinessmanProfile(event.userId());
            case SUPPLIER -> createSupplierProfile(event.userId());
            case PENDING -> logger.info("User {} role is still pending", event.userId());
        }
    }

    private void createBusinessmanProfile(Long userId) {
        try {
            var command = new CreateBusinessmanCommand(
                    userId,
                    null, // Will be filled later by user
                    null, // Will be filled later by user
                    null, // Will be filled later by user
                    null, // Will be filled later by user
                    null  // Will be filled later by user
            );
            businessmanCommandService.handle(command);
            logger.info("Created businessman profile for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error creating businessman profile for user {}: {}", userId, e.getMessage());
        }
    }

    private void createSupplierProfile(Long userId) {
        try {
            var command = new CreateSupplierCommand(
                    userId,
                    null, // Will be filled later by user
                    null, // Will be filled later by user
                    null, // Will be filled later by user
                    null, // Will be filled later by user
                    null  // Will be filled later by user
            );
            supplierCommandService.handle(command);
            logger.info("Created supplier profile for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error creating supplier profile for user {}: {}", userId, e.getMessage());
        }
    }
}