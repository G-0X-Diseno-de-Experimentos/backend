package com.textilflow.platform.profiles.application.internal.eventhandlers;

import com.textilflow.platform.iam.application.services.WelcomeEmailService;
import com.textilflow.platform.iam.domain.model.events.UserRegisteredEvent;
import com.textilflow.platform.iam.domain.model.events.UserRoleUpdatedEvent;
import com.textilflow.platform.iam.domain.model.valueobjects.Roles;
import com.textilflow.platform.iam.interfaces.acl.model.UserData;
import com.textilflow.platform.profiles.domain.model.commands.CreateBusinessmanCommand;
import com.textilflow.platform.profiles.domain.model.commands.CreateSupplierCommand;
import com.textilflow.platform.profiles.domain.services.BusinessmanCommandService;
import com.textilflow.platform.profiles.domain.services.SupplierCommandService;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegisteredEventHandlerTest {

    @Mock private BusinessmanCommandService businessmanCommandService;
    @Mock private SupplierCommandService supplierCommandService;
    @Mock private WelcomeEmailService welcomeEmailService;
    @Mock private ExternalIamService externalIamService;

    @InjectMocks
    private UserRegisteredEventHandler handler;

    // =========================================================
    // USER REGISTERED EVENT
    // =========================================================

    @Test
    @DisplayName("UserRegisteredEvent debe enviar email de bienvenida (AAA)")
    void userRegistered_ShouldSendWelcomeEmail() {
        // Arrange
        var event = mock(UserRegisteredEvent.class);
        when(event.userId()).thenReturn(1L);

        var userData = mock(UserData.class);
        when(userData.email()).thenReturn("test@mail.com");
        when(userData.name()).thenReturn("John");

        when(externalIamService.getUserData(1L)).thenReturn(userData);

        // Act
        handler.on(event);

        // Assert
        verify(externalIamService).getUserData(1L);
        verify(welcomeEmailService).sendWelcomeEmail("test@mail.com", "John");
    }

    @Test
    @DisplayName("UserRegisteredEvent debe manejar error sin romper flujo (AAA)")
    void userRegistered_ShouldHandleException() {
        // Arrange
        var event = mock(UserRegisteredEvent.class);
        when(event.userId()).thenReturn(1L);

        when(externalIamService.getUserData(1L))
                .thenThrow(new RuntimeException("IAM error"));

        // Act
        assertDoesNotThrow(() -> handler.on(event));

        // Assert
        verify(externalIamService).getUserData(1L);
        verifyNoInteractions(welcomeEmailService);
    }

    // =========================================================
    // USER ROLE UPDATED EVENT
    // =========================================================

    @Test
    @DisplayName("BUSINESSMAN debe crear perfil businessman (AAA)")
    void roleUpdated_Businessman_ShouldCreateProfile() {
        // Arrange
        var event = mock(UserRoleUpdatedEvent.class);
        when(event.userId()).thenReturn(10L);
        when(event.newRole()).thenReturn(Roles.BUSINESSMAN);

        // Act
        handler.on(event);

        // Assert
        verify(businessmanCommandService)
                .handle(any(CreateBusinessmanCommand.class));

        verifyNoInteractions(supplierCommandService);
    }

    @Test
    @DisplayName("SUPPLIER debe crear perfil supplier (AAA)")
    void roleUpdated_Supplier_ShouldCreateProfile() {
        // Arrange
        var event = mock(UserRoleUpdatedEvent.class);
        when(event.userId()).thenReturn(20L);
        when(event.newRole()).thenReturn(Roles.SUPPLIER);

        // Act
        handler.on(event);

        // Assert
        verify(supplierCommandService)
                .handle(any(CreateSupplierCommand.class));

        verifyNoInteractions(businessmanCommandService);
    }

    @Test
    @DisplayName("PENDING no debe crear ningún perfil (AAA)")
    void roleUpdated_Pending_ShouldDoNothing() {
        // Arrange
        var event = mock(UserRoleUpdatedEvent.class);
        when(event.userId()).thenReturn(30L);
        when(event.newRole()).thenReturn(Roles.PENDING);

        // Act
        handler.on(event);

        // Assert
        verifyNoInteractions(businessmanCommandService);
        verifyNoInteractions(supplierCommandService);
    }

    // =========================================================
    // EDGE CASES - EXCEPTIONS IN PROFILE CREATION
    // =========================================================

    @Test
    @DisplayName("Businessman creation error no debe romper flujo (AAA)")
    void businessmanCreation_ShouldHandleException() {
        // Arrange
        var event = mock(UserRoleUpdatedEvent.class);
        when(event.userId()).thenReturn(10L);
        when(event.newRole()).thenReturn(Roles.BUSINESSMAN);

        when(businessmanCommandService.handle(any(CreateBusinessmanCommand.class)))
                .thenThrow(new RuntimeException("DB error"));

        // Act
        assertDoesNotThrow(() -> handler.on(event));

        // Assert
        verify(businessmanCommandService)
                .handle(any(CreateBusinessmanCommand.class));
    }

    @Test
    @DisplayName("Supplier creation error no debe romper flujo (AAA)")
    void supplierCreation_ShouldHandleException() {
        // Arrange
        var event = mock(UserRoleUpdatedEvent.class);
        when(event.userId()).thenReturn(20L);
        when(event.newRole()).thenReturn(Roles.SUPPLIER);

        when(supplierCommandService.handle(any(CreateSupplierCommand.class)))
                .thenThrow(new RuntimeException("DB error"));

        // Act
        assertDoesNotThrow(() -> handler.on(event));

        // Assert
        verify(supplierCommandService)
                .handle(any(CreateSupplierCommand.class));
    }
}