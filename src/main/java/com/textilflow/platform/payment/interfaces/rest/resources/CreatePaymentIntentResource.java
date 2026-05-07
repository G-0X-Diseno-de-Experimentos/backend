package com.textilflow.platform.payment.interfaces.rest.resources;

/**
 * Resource for creating payment intent requests
 */
public record CreatePaymentIntentResource(
        Long userId,
        String subscriptionPlan
) {
    public CreatePaymentIntentResource {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid");
        }
        if (subscriptionPlan == null || subscriptionPlan.isBlank()) {
            throw new IllegalArgumentException("Subscription plan cannot be empty");
        }
    }
}