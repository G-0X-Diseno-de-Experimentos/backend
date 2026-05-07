package com.textilflow.platform.payment.domain.model.valueobjects;

import java.math.BigDecimal;

/**
 * Payment amount value object
 * Handles amount validation and conversion to Stripe format (cents)
 */
public record PaymentAmount(BigDecimal amount) {

    public PaymentAmount {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
    }

    /**
     * Convert amount to cents for Stripe API
     * Example: $49.99 -> 4999 cents
     */
    public Long toStripeCents() {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

    /**
     * Get predefined amounts for subscription plans
     */
    public static PaymentAmount forBasicPlan() {
        return new PaymentAmount(new BigDecimal("9.99"));
    }

    public static PaymentAmount forCorporatePlan() {
        return new PaymentAmount(new BigDecimal("49.99"));
    }
}