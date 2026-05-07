package com.textilflow.platform.configuration.application.internal.queryservices;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.domain.model.queries.GetConfigurationByIdQuery;
import com.textilflow.platform.configuration.domain.model.queries.GetConfigurationByUserIdQuery;
import com.textilflow.platform.configuration.domain.services.ConfigurationQueryService;
import com.textilflow.platform.configuration.infrastructure.persistence.jpa.repositories.ConfigurationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Configuration query service implementation
 */
@Service
public class ConfigurationQueryServiceImpl implements ConfigurationQueryService {

    private final ConfigurationRepository configurationRepository;

    public ConfigurationQueryServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public Optional<Configuration> handle(GetConfigurationByIdQuery query) {
        return configurationRepository.findById(query.configurationId());
    }

    @Override
    public Optional<Configuration> handle(GetConfigurationByUserIdQuery query) {
        return configurationRepository.findByUserId_Value(query.userId());
    }
}