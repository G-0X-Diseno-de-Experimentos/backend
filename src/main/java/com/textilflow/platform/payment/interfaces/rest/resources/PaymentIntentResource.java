package com.textilflow.platform.payment.interfaces.rest.resources;

import java.math.BigDecimal;

/**
 * Resource representing payment intent response
 */
public record PaymentIntentResource(
        String clientSecret,
        BigDecimal amount,
        String currency,
        String subscriptionPlan
) {
    public PaymentIntentResource {
        if (clientSecret == null || clientSecret.isBlank()) {
            throw new IllegalArgumentException("Client secret cannot be empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be empty");
        }
        if (subscriptionPlan == null || subscriptionPlan.isBlank()) {
            throw new IllegalArgumentException("Subscription plan cannot be empty");
        }
    }
}