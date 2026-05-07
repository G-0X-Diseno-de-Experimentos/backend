package com.textilflow.platform.configuration.domain.services;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.domain.model.queries.GetConfigurationByIdQuery;
import com.textilflow.platform.configuration.domain.model.queries.GetConfigurationByUserIdQuery;

import java.util.Optional;

/**
 * Configuration query service interface
 */
public interface ConfigurationQueryService {

    /**
     * Handle get configuration by ID query
     */
    Optional<Configuration> handle(GetConfigurationByIdQuery query);

    /**
     * Handle get configuration by user ID query
     */
    Optional<Configuration> handle(GetConfigurationByUserIdQuery query);
}