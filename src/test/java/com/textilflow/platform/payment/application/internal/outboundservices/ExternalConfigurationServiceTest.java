package com.textilflow.platform.payment.application.internal.outboundservices;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.domain.model.commands.ActivateSubscriptionCommand;
import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionPlan;
import com.textilflow.platform.configuration.domain.services.ConfigurationCommandService;
import com.textilflow.platform.configuration.domain.services.ConfigurationQueryService;
import com.textilflow.platform.configuration.interfaces.acl.ConfigurationContextFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import com.textilflow.platform.payment.application.internal.commandservices.PaymentCommandServiceImpl;
import com.textilflow.platform.payment.domain.model.commands.CreatePaymentIntentCommand;
import com.textilflow.platform.payment.domain.model.valueobjects.PaymentAmount;
import com.textilflow.platform.payment.infrastructure.stripe.StripePaymentService;
import com.textilflow.platform.payment.application.internal.outboundservices.acl.ExternalConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalConfigurationServiceTest {

    @Mock
    private ConfigurationContextFacade configurationContextFacade;

    @Mock
    private ConfigurationCommandService configurationCommandService;

    @Mock
    private ConfigurationQueryService configurationQueryService;

    @InjectMocks
    private ExternalConfigurationService service;

    @Test
    @DisplayName("userExists debe delegar al facade correctamente (AAA)")
    void userExists_ShouldReturnTrue_WhenFacadeReturnsTrue() {
        // Arrange
        when(configurationContextFacade.hasConfiguration(1L)).thenReturn(true);

        // Act
        boolean result = service.userExists(1L);

        // Assert
        assertTrue(result);
        verify(configurationContextFacade).hasConfiguration(1L);
    }

    @Test
    @DisplayName("updateSubscriptionPlan debe activar suscripción correctamente (AAA)")
    void updateSubscriptionPlan_ShouldActivateSubscription_WhenCommandSucceeds() {
        // Arrange
        Long userId = 1L;
        String plan = "basic";

        var command = new ActivateSubscriptionCommand(
                userId,
                SubscriptionPlan.fromString(plan)
        );

        when(configurationCommandService.handle(any(ActivateSubscriptionCommand.class)))
                .thenReturn(Optional.of(mock(Configuration.class)));

        // Act
        service.updateSubscriptionPlan(userId, plan);

        // Assert
        verify(configurationCommandService).handle(any(ActivateSubscriptionCommand.class));
    }

    @Test
    @DisplayName("updateSubscriptionPlan debe lanzar excepción cuando resultado es vacío (AAA)")
    void updateSubscriptionPlan_ShouldThrow_WhenResultIsEmpty() {
        // Arrange
        Long userId = 1L;

        when(configurationCommandService.handle(any(ActivateSubscriptionCommand.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateSubscriptionPlan(userId, "basic"));

        assertTrue(ex.getMessage().contains("Failed to activate subscription"));
        verify(configurationCommandService).handle(any(ActivateSubscriptionCommand.class));
    }

    @Test
    @DisplayName("updateSubscriptionPlan debe lanzar RuntimeException si falla el command service (AAA)")
    void updateSubscriptionPlan_ShouldThrow_WhenExceptionOccurs() {
        // Arrange
        Long userId = 1L;

        when(configurationCommandService.handle(any(ActivateSubscriptionCommand.class)))
                .thenThrow(new RuntimeException("DB error"));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateSubscriptionPlan(userId, "basic"));

        assertTrue(ex.getMessage().contains("Failed to activate subscription"));
    }

    @Test
    @DisplayName("getCurrentSubscriptionPlan debe delegar correctamente (AAA)")
    void getCurrentSubscriptionPlan_ShouldReturnPlanFromFacade() {
        // Arrange
        when(configurationContextFacade.getSubscriptionPlan(1L)).thenReturn("basic");

        // Act
        String result = service.getCurrentSubscriptionPlan(1L);

        // Assert
        assertEquals("basic", result);
        verify(configurationContextFacade).getSubscriptionPlan(1L);
    }

    @Test
    @DisplayName("hasActiveSubscription debe delegar correctamente (AAA)")
    void hasActiveSubscription_ShouldReturnValueFromFacade() {
        // Arrange
        when(configurationContextFacade.hasActiveSubscription(1L)).thenReturn(true);

        // Act
        boolean result = service.hasActiveSubscription(1L);

        // Assert
        assertTrue(result);
        verify(configurationContextFacade).hasActiveSubscription(1L);
    }

    @Test
    @DisplayName("requiresPayment debe delegar correctamente (AAA)")
    void requiresPayment_ShouldReturnValueFromFacade() {
        // Arrange
        when(configurationContextFacade.requiresPayment(1L)).thenReturn(true);

        // Act
        boolean result = service.requiresPayment(1L);

        // Assert
        assertTrue(result);
        verify(configurationContextFacade).requiresPayment(1L);
    }

    @Test
    @DisplayName("getSubscriptionStatus debe delegar correctamente (AAA)")
    void getSubscriptionStatus_ShouldReturnStatusFromFacade() {
        // Arrange
        when(configurationContextFacade.getSubscriptionStatus(1L)).thenReturn("ACTIVE");

        // Act
        String result = service.getSubscriptionStatus(1L);

        // Assert
        assertEquals("ACTIVE", result);
        verify(configurationContextFacade).getSubscriptionStatus(1L);
    }
}