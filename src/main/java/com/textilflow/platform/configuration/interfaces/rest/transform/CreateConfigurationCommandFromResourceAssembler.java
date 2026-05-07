package com.textilflow.platform.configuration.interfaces.rest.transform;

import com.textilflow.platform.configuration.domain.model.commands.CreateConfigurationCommand;
import com.textilflow.platform.configuration.domain.model.valueobjects.*;
import com.textilflow.platform.configuration.interfaces.rest.resources.CreateConfigurationResource;

/**
 * Assembler to transform CreateConfigurationResource to CreateConfigurationCommand
 */
public class CreateConfigurationCommandFromResourceAssembler {

    /**
     * Transform CreateConfigurationResource to CreateConfigurationCommand
     */
    public static CreateConfigurationCommand toCommandFromResource(CreateConfigurationResource resource) {
        System.out.println("Converting: " + resource.language() + ", " + resource.viewMode() + ", " + resource.subscriptionPlan());
        return new CreateConfigurationCommand(
                resource.userId(),
                Language.fromString(resource.language()),
                ViewMode.fromString(resource.viewMode()),
                resource.subscriptionPlan() != null ?
                        SubscriptionPlan.fromString(resource.subscriptionPlan()) :
                        SubscriptionPlan.BASIC
                // NOTE: subscriptionStatus always starts as PENDING by default in the entity constructor
                // We don't pass it here because new configurations should always be PENDING
        );
    }
}