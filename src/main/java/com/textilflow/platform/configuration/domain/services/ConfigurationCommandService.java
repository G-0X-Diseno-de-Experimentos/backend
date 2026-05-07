package com.textilflow.platform.configuration.domain.services;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.domain.model.commands.ActivateSubscriptionCommand;
import com.textilflow.platform.configuration.domain.model.commands.CreateConfigurationCommand;
import com.textilflow.platform.configuration.domain.model.commands.UpdateConfigurationCommand;

import java.util.Optional;

/**
 * Configuration command service interface
 */
public interface ConfigurationCommandService {

    /**
     * Handle create configuration command
     */
    Optional<Configuration> handle(CreateConfigurationCommand command);

    /**
     * Handle update configuration command
     */
    Optional<Configuration> handle(UpdateConfigurationCommand command);

    /**
     * Handle activate subscription command
     * Used after successful payment to activate user subscription
     */
    Optional<Configuration> handle(ActivateSubscriptionCommand command);
}