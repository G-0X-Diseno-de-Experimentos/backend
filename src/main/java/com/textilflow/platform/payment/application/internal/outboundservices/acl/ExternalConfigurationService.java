package com.textilflow.platform.payment.application.internal.outboundservices.acl;

import com.textilflow.platform.configuration.interfaces.acl.ConfigurationContextFacade;
import com.textilflow.platform.configuration.domain.services.ConfigurationCommandService;
import com.textilflow.platform.configuration.domain.model.commands.ActivateSubscriptionCommand;
import com.textilflow.platform.configuration.domain.model.queries.GetConfigurationByUserIdQuery;
import com.textilflow.platform.configuration.domain.services.ConfigurationQueryService;
import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionPlan;
import org.springframework.stereotype.Service;

/**
 * External configuration service for payment context
 * Provides anti-corruption layer to interact with Configuration bounded context
 */
@Service
public class ExternalConfigurationService {

    private final ConfigurationContextFacade configurationContextFacade;
    private final ConfigurationCommandService configurationCommandService;
    private final ConfigurationQueryService configurationQueryService;

    public ExternalConfigurationService(ConfigurationContextFacade configurationContextFacade,
                                        ConfigurationCommandService configurationCommandService,
                                        ConfigurationQueryService configurationQueryService) {
        this.configurationContextFacade = configurationContextFacade;
        this.configurationCommandService = configurationCommandService;
        this.configurationQueryService = configurationQueryService;
    }

    /**
     * Check if user exists in the system
     */
    public boolean userExists(Long userId) {
        return configurationContextFacade.hasConfiguration(userId);
    }

    /**
     * *** UPDATED: Activate subscription after successful payment ***
     * Uses the new ActivateSubscriptionCommand for proper payment flow
     */
    public void updateSubscriptionPlan(Long userId, String subscriptionPlan) {
        System.out.println("=== Activating subscription via Configuration context ===");
        System.out.println("User ID: " + userId);
        System.out.println("New subscription plan: " + subscriptionPlan);

        try {
            // Use the new ActivateSubscriptionCommand for payment success
            var activateCommand = new ActivateSubscriptionCommand(
                    userId,
                    SubscriptionPlan.fromString(subscriptionPlan)
            );

            // Execute activation (sets plan + status = ACTIVE)
            var result = configurationCommandService.handle(activateCommand);

            if (result.isEmpty()) {
                throw new RuntimeException("Failed to activate subscription for user: " + userId);
            }

            System.out.println("✅ Subscription activated successfully!");
            System.out.println("   Plan: " + subscriptionPlan);
            System.out.println("   Status: ACTIVE");

        } catch (Exception e) {
            System.err.println("❌ Error activating subscription: " + e.getMessage());
            throw new RuntimeException("Failed to activate subscription for user: " + userId, e);
        }
    }

    /**
     * Get current subscription plan for user
     */
    public String getCurrentSubscriptionPlan(Long userId) {
        return configurationContextFacade.getSubscriptionPlan(userId);
    }

    /**
     * Check if user has active subscription
     */
    public boolean hasActiveSubscription(Long userId) {
        return configurationContextFacade.hasActiveSubscription(userId);
    }

    /**
     * *** NEW: Check if user requires payment ***
     */
    public boolean requiresPayment(Long userId) {
        return configurationContextFacade.requiresPayment(userId);
    }

    /**
     * *** NEW: Get subscription status ***
     */
    public String getSubscriptionStatus(Long userId) {
        return configurationContextFacade.getSubscriptionStatus(userId);
    }
}