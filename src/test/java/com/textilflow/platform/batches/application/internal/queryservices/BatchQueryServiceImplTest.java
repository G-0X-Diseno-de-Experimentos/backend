package com.textilflow.platform.batches.application.internal.queryservices;

import com.textilflow.platform.batches.domain.model.aggregates.Batch;
import com.textilflow.platform.batches.domain.model.queries.*;
import com.textilflow.platform.batches.infraestructure.persistence.repositories.BatchRepository;
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
class BatchQueryServiceImplTest {

    @Mock private BatchRepository batchRepository;

    @InjectMocks private BatchQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetBatchByIdQuery) should return optional from repository (AAA)")
    void handle_GetBatchById_ShouldReturnOptional() {
        // Arrange
        Batch expected = mock(Batch.class);
        when(batchRepository.findById(1L)).thenReturn(Optional.of(expected));
        var query = new GetBatchByIdQuery(1L);

        // Act
        var actual = service.handle(query);

        // Assert
        assertTrue(actual.isPresent());
        assertSame(expected, actual.get());
        verify(batchRepository).findById(1L);
        verifyNoMoreInteractions(batchRepository);
    }

    @Test
    @DisplayName("handle(GetAllBatchesQuery) should return list from repository (AAA)")
    void handle_GetAllBatches_ShouldReturnList() {
        // Arrange
        Batch a = mock(Batch.class);
        Batch b = mock(Batch.class);
        when(batchRepository.findAll()).thenReturn(List.of(a, b));
        var query = new GetAllBatchesQuery();

        // Act
        var actual = service.handle(query);

        // Assert
        assertEquals(2, actual.size());
        assertSame(a, actual.get(0));
        assertSame(b, actual.get(1));
        verify(batchRepository).findAll();
        verifyNoMoreInteractions(batchRepository);
    }

    @Test
    @DisplayName("handle(GetBatchesBySupplierIdQuery) should delegate to repository (AAA)")
    void handle_GetBatchesBySupplierId_ShouldDelegate() {
        // Arrange
        Batch a = mock(Batch.class);
        when(batchRepository.findBySupplierId(20L)).thenReturn(List.of(a));
        var query = new GetBatchesBySupplierIdQuery(20L);

        // Act
        var actual = service.handle(query);

        // Assert
        assertEquals(1, actual.size());
        assertSame(a, actual.get(0));
        verify(batchRepository).findBySupplierId(20L);
        verifyNoMoreInteractions(batchRepository);
    }

    @Test
    @DisplayName("handle(GetBatchesByBusinessmanIdQuery) should delegate to repository (AAA)")
    void handle_GetBatchesByBusinessmanId_ShouldDelegate() {
        // Arrange
        Batch a = mock(Batch.class);
        when(batchRepository.findByBusinessmanId(10L)).thenReturn(List.of(a));
        var query = new GetBatchesByBusinessmanIdQuery(10L);

        // Act
        var actual = service.handle(query);

        // Assert
        assertEquals(1, actual.size());
        assertSame(a, actual.get(0));
        verify(batchRepository).findByBusinessmanId(10L);
        verifyNoMoreInteractions(batchRepository);
    }
}