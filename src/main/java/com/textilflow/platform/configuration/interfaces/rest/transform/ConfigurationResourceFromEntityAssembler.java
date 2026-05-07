package com.textilflow.platform.configuration.interfaces.rest.transform;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.interfaces.rest.resources.ConfigurationResource;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Assembler to transform Configuration entity to ConfigurationResource
 */
public class ConfigurationResourceFromEntityAssembler {

    /**
     * Transform Configuration entity to ConfigurationResource
     */
    public static ConfigurationResource toResourceFromEntity(Configuration entity) {
        return new ConfigurationResource(
                entity.getId(),
                entity.getUserIdValue(),
                entity.getLanguage().getValue(),
                entity.getViewMode().getValue(),
                entity.getSubscriptionPlan() != null ? entity.getSubscriptionPlan().getValue() : null,
                entity.getSubscriptionStatus() != null ? entity.getSubscriptionStatus().name() : "PENDING", // *** NUEVO CAMPO ***
                entity.getSubscriptionStartDate(),
                entity.getCreatedAt() != null ?
                        entity.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null,
                entity.getUpdatedAt() != null ?
                        entity.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null
        );
    }
}