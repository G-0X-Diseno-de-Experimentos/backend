package com.textilflow.platform.profiles.application.internal.queryservices;


import com.textilflow.platform.profiles.domain.model.queries.GetAllSuppliersQuery;

import com.textilflow.platform.profiles.domain.model.queries.GetSupplierByUserIdQuery;

import com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories.SupplierRepository;
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
class SupplierQueryServiceImplTest {

    @Mock
    private SupplierRepository repository;

    @InjectMocks
    private SupplierQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetSupplierByUserIdQuery) debe retornar empty si no existe")
    void shouldReturnEmpty_WhenNotFound() {

        when(repository.findByUserId(1L)).thenReturn(Optional.empty());

        var result = service.handle(new GetSupplierByUserIdQuery(1L));

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("handle(GetAllSuppliersQuery) debe retornar lista vacía")
    void shouldReturnAllSuppliers() {

        when(repository.findAll()).thenReturn(List.of());

        var result = service.handle(new GetAllSuppliersQuery());

        assertNotNull(result);
    }
}