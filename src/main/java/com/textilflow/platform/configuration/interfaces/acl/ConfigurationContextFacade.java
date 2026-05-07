package com.textilflow.platform.configuration.interfaces.acl;

/**
 * Configuration context facade interface
 * Allows other bounded contexts to access configuration data
 */
public interface ConfigurationContextFacade {

    /**
     * Check if user has configuration
     */
    boolean hasConfiguration(Long userId);

    /**
     * Get user language preference
     */
    String getUserLanguage(Long userId);

    /**
     * Get user theme preference
     */
    String getUserTheme(Long userId);

    /**
     * Get user subscription plan
     */
    String getSubscriptionPlan(Long userId);

    /**
     * Check if user has active subscription
     */
    boolean hasActiveSubscription(Long userId);

    // *** NUEVOS MÉTODOS PARA SUBSCRIPTION STATUS ***

    /**
     * Get user subscription status
     */
    String getSubscriptionStatus(Long userId);

    /**
     * Check if user requires payment (status is PENDING)
     */
    boolean requiresPayment(Long userId);

    /**
     * Check if subscription is expired
     */
    boolean isSubscriptionExpired(Long userId);
}