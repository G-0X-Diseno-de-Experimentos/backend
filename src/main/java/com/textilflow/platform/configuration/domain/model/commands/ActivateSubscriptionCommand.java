package com.textilflow.platform.configuration.domain.model.commands;

import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionPlan;

/**
 * Activate subscription command
 * Command specifically for activating a subscription after successful payment
 */
public record ActivateSubscriptionCommand(
        Long userId,
        SubscriptionPlan subscriptionPlan
) {
}