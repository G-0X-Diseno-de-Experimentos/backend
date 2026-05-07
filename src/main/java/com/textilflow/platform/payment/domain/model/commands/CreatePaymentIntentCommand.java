package com.textilflow.platform.payment.domain.model.commands;

import com.textilflow.platform.payment.domain.model.valueobjects.PaymentAmount;

/**
 * Command to create payment intent for subscription upgrade
 */
public record CreatePaymentIntentCommand(
        Long userId,
        String subscriptionPlan, // "basic" or "corporate"
        PaymentAmount amount
) {

    public CreatePaymentIntentCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid");
        }
        if (subscriptionPlan == null || subscriptionPlan.isBlank()) {
            throw new IllegalArgumentException("Subscription plan cannot be empty");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Payment amount cannot be null");
        }

        // Validate subscription plan
        if (!subscriptionPlan.equals("basic") && !subscriptionPlan.equals("corporate")) {
            throw new IllegalArgumentException("Invalid subscription plan: " + subscriptionPlan);
        }
    }
}
