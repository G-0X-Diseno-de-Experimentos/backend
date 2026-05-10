package com.textilflow.platform.configuration.application.internal.queryservices;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.domain.model.queries.GetConfigurationByIdQuery;
import com.textilflow.platform.configuration.domain.model.queries.GetConfigurationByUserIdQuery;
import com.textilflow.platform.configuration.infrastructure.persistence.jpa.repositories.ConfigurationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigurationQueryServiceImplTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    @InjectMocks
    private ConfigurationQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetConfigurationByIdQuery) debe delegar la búsqueda al repositorio (AAA)")
    void handle_GetById_ShouldReturnFromRepository() {
        // Arrange
        Configuration expected = mock(Configuration.class);
        when(configurationRepository.findById(1L)).thenReturn(Optional.of(expected));
        var query = new GetConfigurationByIdQuery(1L);

        // Act
        var result = service.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertSame(expected, result.get());
        verify(configurationRepository).findById(1L);
        verifyNoMoreInteractions(configurationRepository);
    }

    @Test
    @DisplayName("handle(GetConfigurationByUserIdQuery) debe delegar la búsqueda al repositorio (AAA)")
    void handle_GetByUserId_ShouldReturnFromRepository() {
        // Arrange
        Configuration expected = mock(Configuration.class);
        when(configurationRepository.findByUserId_Value(100L)).thenReturn(Optional.of(expected));
        var query = new GetConfigurationByUserIdQuery(100L);

        // Act
        var result = service.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertSame(expected, result.get());
        verify(configurationRepository).findByUserId_Value(100L);
        verifyNoMoreInteractions(configurationRepository);
    }
}