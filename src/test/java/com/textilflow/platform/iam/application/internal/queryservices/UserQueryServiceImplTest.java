package com.textilflow.platform.iam.application.internal.queryservices;

import com.textilflow.platform.iam.domain.model.aggregates.User;
import com.textilflow.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.textilflow.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.textilflow.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
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
class UserQueryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetUserByIdQuery) debe delegar la búsqueda al repositorio (AAA)")
    void handle_GetUserById_ShouldReturnOptional() {
        // Arrange
        User expected = mock(User.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expected));
        var query = new GetUserByIdQuery(1L);

        // Act
        var result = service.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertSame(expected, result.get());
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(GetUserByEmailQuery) debe delegar la búsqueda al repositorio (AAA)")
    void handle_GetUserByEmail_ShouldReturnOptional() {
        // Arrange
        User expected = mock(User.class);
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(expected));
        var query = new GetUserByEmailQuery("test@mail.com");

        // Act
        var result = service.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertSame(expected, result.get());
        verify(userRepository).findByEmail("test@mail.com");
        verifyNoMoreInteractions(userRepository);
    }
}