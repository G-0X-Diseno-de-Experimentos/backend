package com.textilflow.platform.iam.application.internal.commandservices;

import com.textilflow.platform.iam.application.services.PasswordResetService;
import com.textilflow.platform.iam.domain.model.aggregates.User;
import com.textilflow.platform.iam.domain.model.commands.*;
import com.textilflow.platform.iam.domain.model.events.UserRegisteredEvent;
import com.textilflow.platform.iam.domain.model.events.UserRoleUpdatedEvent;
import com.textilflow.platform.iam.domain.model.valueobjects.Roles;
import com.textilflow.platform.iam.infrastructure.hashing.HashingService;
import com.textilflow.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.textilflow.platform.iam.infrastructure.tokens.TokenService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private HashingService hashingService;
    @Mock private TokenService tokenService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private PasswordResetService passwordResetService;

    @InjectMocks private UserCommandServiceImpl service;

    @Test
    @DisplayName("handle(SignUpCommand) debe registrar al usuario exitosamente (AAA)")
    void handle_SignUp_ShouldRegisterUserSuccessfully() {
        // Arrange
        var command = new SignUpCommand("John Doe", "john@mail.com", "secret", "Peru", "Lima", "Av. Pardo", "123456", Roles.PENDING);
        when(userRepository.existsByEmail("john@mail.com")).thenReturn(false);
        when(hashingService.encode("secret")).thenReturn("encoded_secret");

        User savedUserMock = mock(User.class);
        when(savedUserMock.getId()).thenReturn(1L);
        when(savedUserMock.getEmail()).thenReturn("john@mail.com");
        when(savedUserMock.getName()).thenReturn("John Doe");
        when(savedUserMock.getCountry()).thenReturn("Peru");
        when(savedUserMock.getCity()).thenReturn("Lima");
        when(savedUserMock.getAddress()).thenReturn("Av. Pardo");
        when(savedUserMock.getPhone()).thenReturn("123456");

        when(userRepository.save(any(User.class))).thenReturn(savedUserMock);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(savedUserMock, result.get());
        verify(userRepository).existsByEmail("john@mail.com");
        verify(hashingService).encode("secret");
        verify(userRepository).save(any(User.class));
        verify(eventPublisher).publishEvent(any(UserRegisteredEvent.class));
        verifyNoMoreInteractions(userRepository, hashingService, eventPublisher);
    }

    @Test
    @DisplayName("handle(SignUpCommand) debe lanzar excepción si el email ya existe (AAA)")
    void handle_SignUp_ShouldThrow_WhenEmailExists() {
        // Arrange
        var command = new SignUpCommand("John Doe", "john@mail.com", "secret", "Peru", "Lima", "Av. Pardo", "123456", Roles.PENDING);
        when(userRepository.existsByEmail("john@mail.com")).thenReturn(true);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Email already exists"));
        verify(userRepository).existsByEmail("john@mail.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(hashingService, eventPublisher);
    }

    @Test
    @DisplayName("handle(SignInCommand) debe autenticar al usuario exitosamente (AAA)")
    void handle_SignIn_ShouldAuthenticateSuccessfully() {
        // Arrange
        var command = new SignInCommand("john@mail.com", "secret");
        User user = mock(User.class);
        when(user.getPassword()).thenReturn("encoded_secret");
        when(user.getEmail()).thenReturn("john@mail.com");

        when(userRepository.findByEmail("john@mail.com")).thenReturn(Optional.of(user));
        when(hashingService.matches("secret", "encoded_secret")).thenReturn(true);
        when(tokenService.generateToken("john@mail.com")).thenReturn("jwt-token");

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("jwt-token", result.get().getRight());
        assertSame(user, result.get().getLeft());
        verify(userRepository).findByEmail("john@mail.com");
        verify(hashingService).matches("secret", "encoded_secret");
        verify(tokenService).generateToken("john@mail.com");
    }

    @Test
    @DisplayName("handle(SignInCommand) debe lanzar excepción si el usuario no existe (AAA)")
    void handle_SignIn_ShouldThrow_WhenUserNotFound() {
        // Arrange
        var command = new SignInCommand("unknown@mail.com", "secret");
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("User not found"));
        verify(userRepository).findByEmail("unknown@mail.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(hashingService, tokenService);
    }

    @Test
    @DisplayName("handle(SignInCommand) debe lanzar excepción si la contraseña es incorrecta (AAA)")
    void handle_SignIn_ShouldThrow_WhenPasswordIsInvalid() {
        // Arrange
        var command = new SignInCommand("john@mail.com", "wrong_secret");
        User user = mock(User.class);
        when(user.getPassword()).thenReturn("encoded_secret");

        when(userRepository.findByEmail("john@mail.com")).thenReturn(Optional.of(user));
        when(hashingService.matches("wrong_secret", "encoded_secret")).thenReturn(false);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Invalid password"));
        verify(userRepository).findByEmail("john@mail.com");
        verify(hashingService).matches("wrong_secret", "encoded_secret");
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("handle(UpdateUserRoleCommand) debe actualizar el rol exitosamente (AAA)")
    void handle_UpdateUserRole_ShouldUpdateSuccessfully() {
        // Arrange
        // Corrección: Se utiliza Roles.BUSINESSMAN en lugar de Roles.ROLE_BUSINESSMAN
        var command = new UpdateUserRoleCommand(1L, Roles.BUSINESSMAN);
        User user = mock(User.class);
        when(user.getRole()).thenReturn(Roles.PENDING);
        when(user.getId()).thenReturn(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(user, result.get());
        verify(userRepository).findById(1L);
        verify(user).updateRole(Roles.BUSINESSMAN); // Corrección
        verify(userRepository).save(user);
        verify(eventPublisher).publishEvent(any(UserRoleUpdatedEvent.class));
    }

    @Test
    @DisplayName("handle(UpdateUserRoleCommand) debe lanzar excepción si no se encuentra el usuario (AAA)")
    void handle_UpdateUserRole_ShouldThrow_WhenNotFound() {
        // Arrange
        var command = new UpdateUserRoleCommand(1L, Roles.BUSINESSMAN); // Corrección
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> service.handle(command));
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateUserDataCommand) debe actualizar los datos personales exitosamente (AAA)")
    void handle_UpdateUserData_ShouldUpdateSuccessfully() {
        // Arrange
        var command = new UpdateUserDataCommand(1L, "Jane Doe", "jane@mail.com", "Peru", "Arequipa", "Calle 1", "999888");
        User user = mock(User.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        verify(userRepository).findById(1L);
        verify(user).setName("Jane Doe");
        verify(user).setEmail("jane@mail.com");
        verify(user).setCountry("Peru");
        verify(user).setCity("Arequipa");
        verify(user).setAddress("Calle 1");
        verify(user).setPhone("999888");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("handle(ForgotPasswordCommand) debe enviar el correo de recuperación (AAA)")
    void handle_ForgotPassword_ShouldSendEmail() {
        // Arrange
        var command = new ForgotPasswordCommand("john@mail.com");
        when(passwordResetService.sendPasswordResetEmail("john@mail.com")).thenReturn(true);

        // Act
        boolean result = service.handle(command);

        // Assert
        assertTrue(result);
        verify(passwordResetService).sendPasswordResetEmail("john@mail.com");
    }

    @Test
    @DisplayName("handle(ResetPasswordCommand) debe fallar si el token es inválido (AAA)")
    void handle_ResetPassword_ShouldReturnFalse_WhenTokenIsInvalid() {
        // Arrange
        var command = new ResetPasswordCommand("invalid-token", "new_secret");
        when(passwordResetService.validateResetToken("invalid-token")).thenReturn(null);

        // Act
        boolean result = service.handle(command);

        // Assert
        assertFalse(result);
        verify(passwordResetService).validateResetToken("invalid-token");
        verifyNoInteractions(userRepository, hashingService);
    }

    @Test
    @DisplayName("handle(ResetPasswordCommand) debe restablecer la contraseña exitosamente (AAA)")
    void handle_ResetPassword_ShouldResetSuccessfully() {
        // Arrange
        var command = new ResetPasswordCommand("valid-token", "new_secret");
        User user = mock(User.class);

        when(passwordResetService.validateResetToken("valid-token")).thenReturn("john@mail.com");
        when(userRepository.findByEmail("john@mail.com")).thenReturn(Optional.of(user));
        when(hashingService.encode("new_secret")).thenReturn("encoded_new_secret");

        // Act
        boolean result = service.handle(command);

        // Assert
        assertTrue(result);
        verify(passwordResetService).validateResetToken("valid-token");
        verify(userRepository).findByEmail("john@mail.com");
        verify(hashingService).encode("new_secret");
        verify(user).setPassword("encoded_new_secret");
        verify(userRepository).save(user);
    }
}