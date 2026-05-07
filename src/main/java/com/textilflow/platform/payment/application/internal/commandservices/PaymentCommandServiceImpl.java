package com.textilflow.platform.payment.application.internal.commandservices;

import com.textilflow.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.textilflow.platform.payment.domain.services.PaymentService;
import com.textilflow.platform.payment.infrastructure.stripe.StripePaymentService;
import com.textilflow.platform.payment.application.internal.outboundservices.acl.ExternalConfigurationService;
import org.springframework.stereotype.Service;

/**
 * Payment commands service implementation
 * Orchestrates payment operations and coordinates with external services
 */
@Service
public class PaymentCommandServiceImpl implements PaymentService {

    private final StripePaymentService stripePaymentService;
    private final ExternalConfigurationService externalConfigurationService;

    public PaymentCommandServiceImpl(StripePaymentService stripePaymentService,
                                     ExternalConfigurationService externalConfigurationService) {
        this.stripePaymentService = stripePaymentService;
        this.externalConfigurationService = externalConfigurationService;
    }

    @Override
    public String createPaymentIntent(CreatePaymentIntentCommand command) {
        System.out.println("=== Creating payment intent ===");
        System.out.println("User ID: " + command.userId());
        System.out.println("Subscription Plan: " + command.subscriptionPlan());
        System.out.println("Amount: $" + command.amount().amount());

        // Validate user exists and has configuration
        boolean userExists = externalConfigurationService.userExists(command.userId());
        if (!userExists) {
            throw new IllegalArgumentException("User with ID " + command.userId() + " does not exist");
        }

        // Create payment intent via Stripe
        String clientSecret = stripePaymentService.createPaymentIntent(
                command.amount().toStripeCents(),
                command.userId(),
                command.subscriptionPlan()
        );

        System.out.println("Payment intent created successfully");
        return clientSecret;
    }

    @Override
    public void handlePaymentSuccess(String paymentIntentId, Long userId, String subscriptionPlan) {
        System.out.println("=== Handling payment success ===");
        System.out.println("Payment Intent ID: " + paymentIntentId);
        System.out.println("User ID: " + userId);
        System.out.println("New Subscription Plan: " + subscriptionPlan);

        try {
            // Update user's subscription plan via Configuration bounded context
            externalConfigurationService.updateSubscriptionPlan(userId, subscriptionPlan);
            System.out.println("Subscription updated successfully");
        } catch (Exception e) {
            System.err.println("Error updating subscription: " + e.getMessage());
            throw new RuntimeException("Failed to update user subscription", e);
        }
    }
}