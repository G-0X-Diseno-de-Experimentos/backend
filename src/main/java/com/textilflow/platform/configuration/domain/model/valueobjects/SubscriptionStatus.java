package com.textilflow.platform.configuration.domain.model.valueobjects;

/**
 * Subscription status enum
 * Represents the payment status of a user's subscription
 */
public enum SubscriptionStatus {
    PENDING,    // Usuario registrado pero sin pagar
    ACTIVE,     // Suscripción pagada y activa
    EXPIRED;    // Suscripción expirada

    /**
     * Get the string value of the enum
     */
    public String getValue() {
        return name().toLowerCase();
    }

    /**
     * Create SubscriptionStatus from string
     */
    public static SubscriptionStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            return PENDING;
        }

        try {
            return SubscriptionStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}