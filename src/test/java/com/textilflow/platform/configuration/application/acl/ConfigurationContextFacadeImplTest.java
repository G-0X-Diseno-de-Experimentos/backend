package com.textilflow.platform.configuration.application.acl;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.domain.model.queries.GetConfigurationByUserIdQuery;
import com.textilflow.platform.configuration.domain.model.valueobjects.Language;
import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionPlan;
import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionStatus;
import com.textilflow.platform.configuration.domain.model.valueobjects.ViewMode;
import com.textilflow.platform.configuration.domain.services.ConfigurationQueryService;
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
class ConfigurationContextFacadeImplTest {

    @Mock
    private ConfigurationQueryService configurationQueryService;

    @InjectMocks
    private ConfigurationContextFacadeImpl facade;

    @Test
    @DisplayName("hasConfiguration debe retornar true si la consulta encuentra resultados (AAA)")
    void hasConfiguration_ShouldReturnTrue_WhenExists() {
        // Arrange
        when(configurationQueryService.handle(any(GetConfigurationByUserIdQuery.class)))
                .thenReturn(Optional.of(mock(Configuration.class)));

        // Act
        boolean exists = facade.hasConfiguration(100L);

        // Assert
        assertTrue(exists);
        verify(configurationQueryService).handle(any(GetConfigurationByUserIdQuery.class));
    }

    @Test
    @DisplayName("Valores por defecto deben ser devueltos si la configuración no existe (AAA)")
    void defaultValues_ShouldReturn_WhenConfigIsEmpty() {
        // Arrange
        when(configurationQueryService.handle(any(GetConfigurationByUserIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertEquals("es", facade.getUserLanguage(100L));
        assertEquals("auto", facade.getUserTheme(100L));
        assertEquals("basic", facade.getSubscriptionPlan(100L));
        assertFalse(facade.hasActiveSubscription(100L));
        assertEquals("pending", facade.getSubscriptionStatus(100L));
        assertTrue(facade.requiresPayment(100L));
        assertFalse(facade.isSubscriptionExpired(100L));
    }

    @Test
    @DisplayName("Debe mapear los valores reales desde la entidad Configuration exitosamente (AAA)")
    void mapRealValues_FromConfiguration() {
        // Arrange
        Configuration config = mock(Configuration.class);
        when(config.getLanguage()).thenReturn(Language.EN);
        when(config.getViewMode()).thenReturn(ViewMode.DARK);
        // Se cambia a CORPORATE en lugar de PREMIUM
        when(config.getSubscriptionPlan()).thenReturn(SubscriptionPlan.CORPORATE);
        when(config.getSubscriptionStatus()).thenReturn(SubscriptionStatus.ACTIVE);
        when(config.isSubscriptionExpired()).thenReturn(false);
        when(config.requiresPayment()).thenReturn(false);

        when(configurationQueryService.handle(any(GetConfigurationByUserIdQuery.class)))
                .thenReturn(Optional.of(config));

        // Act & Assert
        assertEquals("en", facade.getUserLanguage(100L));
        assertEquals("dark", facade.getUserTheme(100L));
        assertEquals("corporate", facade.getSubscriptionPlan(100L));
        assertTrue(facade.hasActiveSubscription(100L));
        assertEquals("active", facade.getSubscriptionStatus(100L));
        assertFalse(facade.requiresPayment(100L));
    }
}