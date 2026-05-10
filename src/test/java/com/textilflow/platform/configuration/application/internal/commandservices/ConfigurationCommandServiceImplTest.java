package com.textilflow.platform.configuration.application.internal.commandservices;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.domain.model.commands.ActivateSubscriptionCommand;
import com.textilflow.platform.configuration.domain.model.commands.CreateConfigurationCommand;
import com.textilflow.platform.configuration.domain.model.commands.UpdateConfigurationCommand;
import com.textilflow.platform.configuration.domain.model.valueobjects.Language;
import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionPlan;
import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionStatus;
import com.textilflow.platform.configuration.domain.model.valueobjects.ViewMode;
import com.textilflow.platform.configuration.infrastructure.persistence.jpa.repositories.ConfigurationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigurationCommandServiceImplTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    @InjectMocks
    private ConfigurationCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateConfigurationCommand) debe crear una configuración exitosamente (AAA)")
    void handle_CreateConfiguration_ShouldSaveAndReturnConfiguration() {
        // Arrange
        var command = new CreateConfigurationCommand(100L, Language.ES, ViewMode.AUTO, SubscriptionPlan.BASIC);
        Configuration savedConfigMock = mock(Configuration.class);
        when(configurationRepository.save(any(Configuration.class))).thenReturn(savedConfigMock);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(savedConfigMock, result.get());
        verify(configurationRepository).save(any(Configuration.class));
        verifyNoMoreInteractions(configurationRepository);
    }

    @Test
    @DisplayName("handle(UpdateConfigurationCommand) debe lanzar excepción si la configuración no existe (AAA)")
    void handle_UpdateConfiguration_ShouldThrow_WhenNotFound() {
        // Arrange
        // Se usa CORPORATE y el orden correcto del constructor canónico
        var command = new UpdateConfigurationCommand(1L, Language.EN, ViewMode.DARK, SubscriptionPlan.CORPORATE, SubscriptionStatus.ACTIVE);
        when(configurationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        var ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("does not exist"));
        verify(configurationRepository).findById(1L);
        verifyNoMoreInteractions(configurationRepository);
    }

    @Test
    @DisplayName("handle(UpdateConfigurationCommand) debe actualizar preferencias y suscripción exitosamente (AAA)")
    void handle_UpdateConfiguration_ShouldUpdateSuccessfully() {
        // Arrange
        var command = new UpdateConfigurationCommand(1L, Language.EN, ViewMode.DARK, SubscriptionPlan.CORPORATE, SubscriptionStatus.ACTIVE);
        Configuration existingConfig = mock(Configuration.class);

        when(configurationRepository.findById(1L)).thenReturn(Optional.of(existingConfig));
        when(configurationRepository.save(existingConfig)).thenReturn(existingConfig);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(existingConfig, result.get());
        verify(configurationRepository).findById(1L);
        verify(existingConfig).activateSubscription(SubscriptionPlan.CORPORATE);
        verify(existingConfig).updateSettings(Language.EN, ViewMode.DARK);
        verify(configurationRepository).save(existingConfig);
    }

    @Test
    @DisplayName("handle(ActivateSubscriptionCommand) debe lanzar excepción si no existe configuración para el usuario (AAA)")
    void handle_ActivateSubscription_ShouldThrow_WhenNotFound() {
        // Arrange
        var command = new ActivateSubscriptionCommand(100L, SubscriptionPlan.CORPORATE);
        when(configurationRepository.findByUserId_Value(100L)).thenReturn(Optional.empty());

        // Act + Assert
        var ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("does not exist"));
        verify(configurationRepository).findByUserId_Value(100L);
        verifyNoMoreInteractions(configurationRepository);
    }

    @Test
    @DisplayName("handle(ActivateSubscriptionCommand) debe activar suscripción exitosamente (AAA)")
    void handle_ActivateSubscription_ShouldActivateSuccessfully() {
        // Arrange
        var command = new ActivateSubscriptionCommand(100L, SubscriptionPlan.CORPORATE);
        Configuration existingConfig = mock(Configuration.class);

        when(configurationRepository.findByUserId_Value(100L)).thenReturn(Optional.of(existingConfig));
        when(configurationRepository.save(existingConfig)).thenReturn(existingConfig);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(existingConfig, result.get());
        verify(configurationRepository).findByUserId_Value(100L);
        verify(existingConfig).activateSubscription(SubscriptionPlan.CORPORATE);
        verify(configurationRepository).save(existingConfig);
    }
}