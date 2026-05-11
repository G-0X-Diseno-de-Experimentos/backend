package com.textilflow.platform.profiles.application.internal.queryservices;


import com.textilflow.platform.profiles.domain.model.queries.GetAllBusinessmenQuery;
import com.textilflow.platform.profiles.domain.model.queries.GetBusinessmanByUserIdQuery;

import com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessmanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessmanQueryServiceImplTest {

    @Mock
    private BusinessmanRepository repository;

    @InjectMocks
    private BusinessmanQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetBusinessmanByUserIdQuery) debe retornar empty si no existe")
    void shouldReturnEmpty_WhenNotFound() {

        // Arrange
        when(repository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act
        var result = service.handle(new GetBusinessmanByUserIdQuery(1L));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("handle(GetAllBusinessmenQuery) debe retornar lista")
    void shouldReturnAllBusinessmen() {

        // Arrange
        when(repository.findAll()).thenReturn(List.of());

        // Act
        var result = service.handle(new GetAllBusinessmenQuery());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}