package com.textilflow.platform.configuration.application.internal.eventhandlers;

import com.textilflow.platform.configuration.domain.model.commands.CreateConfigurationCommand;
import com.textilflow.platform.configuration.domain.model.valueobjects.*;
import com.textilflow.platform.configuration.domain.services.ConfigurationCommandService;
import com.textilflow.platform.iam.domain.model.events.UserRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles user registered events to create default configuration
 */
@Service("configurationUserRegisteredEventHandler")
public class UserRegisteredEventHandler {

    private final ConfigurationCommandService configurationCommandService;

    public UserRegisteredEventHandler(ConfigurationCommandService configurationCommandService) {
        this.configurationCommandService = configurationCommandService;
    }

    @EventListener
    public void on(UserRegisteredEvent event) {


        var createConfigurationCommand = new CreateConfigurationCommand(
                event.userId(),
                Language.ES, // Default language
                ViewMode.AUTO, // Default view mode
                SubscriptionPlan.BASIC // Default subscription
        );

        configurationCommandService.handle(createConfigurationCommand);
    }
}
