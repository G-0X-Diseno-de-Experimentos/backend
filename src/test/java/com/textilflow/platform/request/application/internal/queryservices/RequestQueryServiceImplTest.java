package com.textilflow.platform.request.application.internal.queryservices;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import com.textilflow.platform.request.domain.model.queries.*;
import com.textilflow.platform.request.infrastructure.persistence.jpa.repositories.RequestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestQueryServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetRequestByIdQuery) debe delegar la búsqueda al repositorio por ID (AAA)")
    void handle_GetById_ShouldDelegate() {
        // Arrange
        BusinessSupplierRequest expected = mock(BusinessSupplierRequest.class);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        var query = new GetRequestByIdQuery(1L);

        // Act
        var result = service.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertSame(expected, result.get());
        verify(requestRepository).findById(anyLong());
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    @DisplayName("handle(GetRequestsByBusinessmanIdQuery) debe delegar la búsqueda al repositorio (AAA)")
    void handle_GetByBusinessmanId_ShouldDelegate() {
        // Arrange
        List<BusinessSupplierRequest> expectedList = List.of(mock(BusinessSupplierRequest.class));
        when(requestRepository.findByBusinessmanId(any())).thenReturn(expectedList);

        // Corrección: El constructor del record espera directamente un Long según su definición
        var query = new GetRequestsByBusinessmanIdQuery(10L);

        // Act
        var result = service.handle(query);

        // Assert
        assertEquals(1, result.size());
        verify(requestRepository).findByBusinessmanId(any());
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    @DisplayName("handle(GetRequestsBySupplierIdQuery) debe delegar la búsqueda al repositorio (AAA)")
    void handle_GetBySupplierId_ShouldDelegate() {
        // Arrange
        List<BusinessSupplierRequest> expectedList = List.of(mock(BusinessSupplierRequest.class));
        when(requestRepository.findBySupplierId(any())).thenReturn(expectedList);

        // Corrección: El constructor del record espera directamente un Long según su definición
        var query = new GetRequestsBySupplierIdQuery(20L);

        // Act
        var result = service.handle(query);

        // Assert
        assertEquals(1, result.size());
        verify(requestRepository).findBySupplierId(any());
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    @DisplayName("handle(GetAllRequestsQuery) debe delegar la búsqueda global ordenada al repositorio (AAA)")
    void handle_GetAll_ShouldDelegate() {
        // Arrange
        List<BusinessSupplierRequest> expectedList = List.of(mock(BusinessSupplierRequest.class), mock(BusinessSupplierRequest.class));
        when(requestRepository.findAllOrderByCreatedAtDesc()).thenReturn(expectedList);
        var query = new GetAllRequestsQuery();

        // Act
        var result = service.handle(query);

        // Assert
        assertEquals(2, result.size());
        verify(requestRepository).findAllOrderByCreatedAtDesc();
        verifyNoMoreInteractions(requestRepository);
    }
}