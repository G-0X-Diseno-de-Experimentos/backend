package com.textilflow.platform.configuration.domain.model.commands;

import com.textilflow.platform.configuration.domain.model.valueobjects.*;

/**
 * Create configuration command
 */
public record CreateConfigurationCommand(
        Long userId,
        Language language,
        ViewMode viewMode,
        SubscriptionPlan subscriptionPlan
) {
}
