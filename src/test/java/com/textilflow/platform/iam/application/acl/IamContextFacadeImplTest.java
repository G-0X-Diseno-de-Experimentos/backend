package com.textilflow.platform.iam.application.acl;

import com.textilflow.platform.iam.domain.model.aggregates.User;
import com.textilflow.platform.iam.domain.model.commands.UpdateUserDataCommand;
import com.textilflow.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.textilflow.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.textilflow.platform.iam.domain.services.UserCommandService;
import com.textilflow.platform.iam.domain.services.UserQueryService;
import com.textilflow.platform.iam.interfaces.acl.model.UserData;
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
class IamContextFacadeImplTest {

    @Mock private UserQueryService userQueryService;
    @Mock private UserCommandService userCommandService;

    @InjectMocks private IamContextFacadeImpl facade;

    @Test
    @DisplayName("getUserIdByEmail debe retornar el ID si el usuario existe (AAA)")
    void getUserIdByEmail_ShouldReturnId_WhenExists() {
        // Arrange
        User user = mock(User.class);
        when(user.getId()).thenReturn(100L);
        when(userQueryService.handle(any(GetUserByEmailQuery.class))).thenReturn(Optional.of(user));

        // Act
        Long userId = facade.getUserIdByEmail("test@mail.com");

        // Assert
        assertEquals(100L, userId);
        verify(userQueryService).handle(any(GetUserByEmailQuery.class));
    }

    @Test
    @DisplayName("userExists debe retornar true si la consulta retorna un valor (AAA)")
    void userExists_ShouldReturnTrue_WhenExists() {
        // Arrange
        when(userQueryService.handle(any(GetUserByIdQuery.class))).thenReturn(Optional.of(mock(User.class)));

        // Act
        boolean exists = facade.userExists(100L);

        // Assert
        assertTrue(exists);
        verify(userQueryService).handle(any(GetUserByIdQuery.class));
    }

    @Test
    @DisplayName("getUserRole debe retornar el nombre del rol exacto del enum como string (AAA)")
    void getUserRole_ShouldReturnRoleName() {
        // Arrange
        User user = mock(User.class);
        // Corrección: Devuelve exactamente "BUSINESSMAN" alineado con this.role.name()
        when(user.getRoleName()).thenReturn("BUSINESSMAN");
        when(userQueryService.handle(any(GetUserByIdQuery.class))).thenReturn(Optional.of(user));

        // Act
        String role = facade.getUserRole(100L);

        // Assert
        assertEquals("BUSINESSMAN", role); // Corrección
        verify(userQueryService).handle(any(GetUserByIdQuery.class));
    }

    @Test
    @DisplayName("getUserData debe retornar null si el usuario no es encontrado (AAA)")
    void getUserData_ShouldReturnNull_WhenNotFound() {
        // Arrange
        when(userQueryService.handle(any(GetUserByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        UserData data = facade.getUserData(100L);

        // Assert
        assertNull(data);
        verify(userQueryService).handle(any(GetUserByIdQuery.class));
    }

    @Test
    @DisplayName("getUserData debe mapear correctamente los campos de la entidad (AAA)")
    void getUserData_ShouldMapValuesSuccessfully() {
        // Arrange
        User userMock = mock(User.class);
        when(userMock.getName()).thenReturn("John");
        when(userMock.getEmail()).thenReturn("john@mail.com");
        when(userMock.getCountry()).thenReturn("Peru");
        when(userMock.getCity()).thenReturn("Lima");
        when(userMock.getAddress()).thenReturn("Av. Sol");
        when(userMock.getPhone()).thenReturn("123");

        when(userQueryService.handle(any(GetUserByIdQuery.class))).thenReturn(Optional.of(userMock));

        // Act
        UserData data = facade.getUserData(100L);

        // Assert
        assertNotNull(data);
        assertEquals("John", data.name());
        assertEquals("john@mail.com", data.email());
        assertEquals("Peru", data.country());
        assertEquals("Lima", data.city());
        assertEquals("Av. Sol", data.address());
        assertEquals("123", data.phone());
    }

    @Test
    @DisplayName("updateUserData debe disparar el UpdateUserDataCommand correctamente (AAA)")
    void updateUserData_ShouldDispatchCommand() {
        // Arrange & Act
        facade.updateUserData(100L, "John", "john@mail.com", "Peru", "Lima", "Av. Sol", "123");

        // Assert
        verify(userCommandService).handle(any(UpdateUserDataCommand.class));
        verifyNoMoreInteractions(userCommandService);
    }

    @Test
    @DisplayName("getUserIdByEmail debe retornar null si el usuario no existe (AAA)")
    void getUserIdByEmail_ShouldReturnNull_WhenNotFound() {
        // Arrange
        when(userQueryService.handle(any(GetUserByEmailQuery.class)))
                .thenReturn(Optional.empty());

        // Act
        Long result = facade.getUserIdByEmail("missing@mail.com");

        // Assert
        assertNull(result);
        verify(userQueryService).handle(any(GetUserByEmailQuery.class));
    }

    @Test
    @DisplayName("userExists debe retornar false si el usuario no existe (AAA)")
    void userExists_ShouldReturnFalse_WhenNotFound() {
        // Arrange
        when(userQueryService.handle(any(GetUserByIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act
        boolean result = facade.userExists(999L);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("getUserRole debe retornar null si el usuario no existe (AAA)")
    void getUserRole_ShouldReturnNull_WhenNotFound() {
        // Arrange
        when(userQueryService.handle(any(GetUserByIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act
        String role = facade.getUserRole(999L);

        // Assert
        assertNull(role);
    }

    @Test
    @DisplayName("userExists debe manejar userId null sin romper (AAA)")
    void userExists_ShouldHandleNullUserId() {
        // Arrange
        when(userQueryService.handle(any(GetUserByIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act
        boolean result = facade.userExists(null);

        // Assert
        assertFalse(result);
    }
    @Test
    @DisplayName("getUserRole debe manejar userId null sin romper (AAA)")
    void getUserRole_ShouldHandleNullUserId() {
        // Arrange
        when(userQueryService.handle(any(GetUserByIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act
        String role = facade.getUserRole(null);

        // Assert
        assertNull(role);
    }

    @Test
    @DisplayName("getUserIdByEmail debe manejar email null sin romper (AAA)")
    void getUserIdByEmail_ShouldHandleNullEmail() {
        // Arrange
        when(userQueryService.handle(any(GetUserByEmailQuery.class)))
                .thenReturn(Optional.empty());

        // Act
        Long result = facade.getUserIdByEmail(null);

        // Assert
        assertNull(result);
    }
    @Test
    @DisplayName("updateUserData debe delegar incluso con valores null (AAA)")
    void updateUserData_ShouldDelegateEvenWithNullValues() {
        // Arrange & Act
        facade.updateUserData(1L, null, null, null, null, null, null);

        // Assert
        verify(userCommandService).handle(any(UpdateUserDataCommand.class));
    }


}