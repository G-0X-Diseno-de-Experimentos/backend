package com.textilflow.platform.configuration.application.internal;

import com.textilflow.platform.configuration.application.internal.eventhandlers.UserRegisteredEventHandler;
import com.textilflow.platform.configuration.application.internal.outboundservices.acl.ExternalIamService;
import com.textilflow.platform.configuration.domain.model.commands.CreateConfigurationCommand;
import com.textilflow.platform.configuration.domain.services.ConfigurationCommandService;
import com.textilflow.platform.iam.domain.model.events.UserRegisteredEvent;
import com.textilflow.platform.iam.interfaces.acl.IamContextFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntegrationAdaptersTest {

    @Mock private ConfigurationCommandService configurationCommandService;
    @InjectMocks private UserRegisteredEventHandler eventHandler;

    @Mock private IamContextFacade iamContextFacade;
    @InjectMocks private ExternalIamService externalIamService;

    @Test
    @DisplayName("on(UserRegisteredEvent) debe disparar la creación de una configuración por defecto (AAA)")
    void on_UserRegisteredEvent_ShouldDispatchCommand() {
        // Arrange
        // Se completan los 7 campos que requiere el nuevo UserRegisteredEvent
        var event = new UserRegisteredEvent(
                500L,
                "test@mail.com",
                "John Doe",
                "Peru",
                "Lima",
                "Av. Pardo 123",
                "987654321"
        );

        // Act
        eventHandler.on(event);

        // Assert
        verify(configurationCommandService).handle(any(CreateConfigurationCommand.class));
        verifyNoMoreInteractions(configurationCommandService);
    }

    @Test
    @DisplayName("ExternalIamService debe delegar validaciones correctamente a IamContextFacade (AAA)")
    void externalIamService_Delegations() {
        // Arrange
        when(iamContextFacade.userExists(500L)).thenReturn(true);
        when(iamContextFacade.getUserRole(500L)).thenReturn("ROLE_BUSINESSMAN");

        // Act
        boolean exists = externalIamService.userExists(500L);
        String role = externalIamService.getUserRole(500L);

        // Assert
        assertTrue(exists);
        assertEquals("ROLE_BUSINESSMAN", role);
        verify(iamContextFacade).userExists(500L);
        verify(iamContextFacade).getUserRole(500L);
        verifyNoMoreInteractions(iamContextFacade);
    }
}