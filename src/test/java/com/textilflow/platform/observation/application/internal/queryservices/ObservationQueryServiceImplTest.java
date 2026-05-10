package com.textilflow.platform.observation.application.internal.queryservices;

import com.textilflow.platform.observation.domain.model.aggregates.Observation;
import com.textilflow.platform.observation.domain.model.queries.*;
import com.textilflow.platform.observation.infrastructure.persistence.jpa.repositories.ObservationRepository;
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
class ObservationQueryServiceImplTest {

    @Mock private ObservationRepository observationRepository;

    @InjectMocks private ObservationQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetObservationByIdQuery) debe delegar al repositorio (AAA)")
    void handle_GetById_ShouldDelegateToRepository() {
        // Arrange
        Observation expected = mock(Observation.class);
        when(observationRepository.findById(1L)).thenReturn(Optional.of(expected));
        var query = new GetObservationByIdQuery(1L);

        // Act
        var result = service.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertSame(expected, result.get());
        verify(observationRepository).findById(1L);
    }

    @Test
    @DisplayName("handle(GetObservationsByBatchIdQuery) debe delegar al repositorio (AAA)")
    void handle_GetByBatchId_ShouldDelegateToRepository() {
        // Arrange
        List<Observation> expected = List.of(mock(Observation.class));
        when(observationRepository.findByBatchId(10L)).thenReturn(expected);
        var query = new GetObservationsByBatchIdQuery(10L);

        // Act
        var result = service.handle(query);

        // Assert
        assertEquals(1, result.size());
        verify(observationRepository).findByBatchId(10L);
    }

    @Test
    @DisplayName("handle(GetObservationsByBusinessmanIdQuery) debe delegar al repositorio (AAA)")
    void handle_GetByBusinessmanId_ShouldDelegateToRepository() {
        // Arrange
        List<Observation> expected = List.of(mock(Observation.class));
        when(observationRepository.findByBusinessmanId(100L)).thenReturn(expected);
        var query = new GetObservationsByBusinessmanIdQuery(100L);

        // Act
        var result = service.handle(query);

        // Assert
        assertEquals(1, result.size());
        verify(observationRepository).findByBusinessmanId(100L);
    }

    @Test
    @DisplayName("handle(GetObservationsBySupplierIdQuery) debe delegar al repositorio (AAA)")
    void handle_GetBySupplierId_ShouldDelegateToRepository() {
        // Arrange
        List<Observation> expected = List.of(mock(Observation.class));
        when(observationRepository.findBySupplierId(200L)).thenReturn(expected);
        var query = new GetObservationsBySupplierIdQuery(200L);

        // Act
        var result = service.handle(query);

        // Assert
        assertEquals(1, result.size());
        verify(observationRepository).findBySupplierId(200L);
    }
}