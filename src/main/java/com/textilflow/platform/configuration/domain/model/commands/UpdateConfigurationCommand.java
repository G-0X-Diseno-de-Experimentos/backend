package com.textilflow.platform.configuration.domain.model.commands;

import com.textilflow.platform.configuration.domain.model.valueobjects.Language;
import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionPlan;
import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionStatus;
import com.textilflow.platform.configuration.domain.model.valueobjects.ViewMode;

/**
 * Update configuration command
 * Command to update user configuration settings
 */
public record UpdateConfigurationCommand(
        Long configId,
        Language language,
        ViewMode viewMode,
        SubscriptionPlan subscriptionPlan,
        SubscriptionStatus subscriptionStatus  // *** NEW FIELD ***
) {

    /**
     * Constructor for updating only preferences (without changing subscription)
     */
    public UpdateConfigurationCommand(Long configId, Language language, ViewMode viewMode) {
        this(configId, language, viewMode, null, null);
    }

    /**
     * Constructor for updating only subscription plan
     */
    public UpdateConfigurationCommand(Long configId, SubscriptionPlan subscriptionPlan) {
        this(configId, null, null, subscriptionPlan, null);
    }

    /**
     * *** COMPATIBILITY: Constructor for legacy code (4 parameters) ***
     */
    public UpdateConfigurationCommand(Long configId, Language language, ViewMode viewMode, SubscriptionPlan subscriptionPlan) {
        this(configId, language, viewMode, subscriptionPlan, null);
    }

    /**
     * *** NEW: Constructor for activating subscription (after payment) ***
     */
    public UpdateConfigurationCommand(Long configId, SubscriptionPlan subscriptionPlan, SubscriptionStatus subscriptionStatus) {
        this(configId, null, null, subscriptionPlan, subscriptionStatus);
    }
}