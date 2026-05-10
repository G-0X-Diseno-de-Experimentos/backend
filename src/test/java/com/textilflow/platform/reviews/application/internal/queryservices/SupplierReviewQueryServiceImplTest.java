package com.textilflow.platform.reviews.application.internal.queryservices;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.domain.model.queries.CheckIfBusinessmanReviewedSupplierQuery;
import com.textilflow.platform.reviews.domain.model.queries.GetReviewByIdQuery;
import com.textilflow.platform.reviews.domain.model.queries.GetReviewsBySupplierIdQuery;
import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.SupplierId;
import com.textilflow.platform.reviews.infrastructure.persistence.jpa.repositories.SupplierReviewRepository;
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
class SupplierReviewQueryServiceImplTest {

    @Mock
    private SupplierReviewRepository supplierReviewRepository;

    @InjectMocks
    private SupplierReviewQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetReviewsBySupplierIdQuery) debe delegar la búsqueda por supplierId (AAA)")
    void handle_GetBySupplierId_ShouldDelegate() {
        // Arrange
        var query = mock(GetReviewsBySupplierIdQuery.class);
        when(query.supplierId()).thenReturn(10L);

        List<SupplierReview> expectedList = List.of(mock(SupplierReview.class));
        when(supplierReviewRepository.findBySupplierId(new SupplierId(10L))).thenReturn(expectedList);

        // Act
        var result = service.handle(query);

        // Assert
        assertEquals(1, result.size());
        verify(supplierReviewRepository).findBySupplierId(new SupplierId(10L));
    }

    @Test
    @DisplayName("handle(GetReviewByIdQuery) debe delegar la búsqueda por reviewId (AAA)")
    void handle_GetById_ShouldDelegate() {
        // Arrange
        var query = mock(GetReviewByIdQuery.class);
        when(query.reviewId()).thenReturn(1L);

        SupplierReview expected = mock(SupplierReview.class);
        when(supplierReviewRepository.findById(1L)).thenReturn(Optional.of(expected));

        // Act
        var result = service.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertSame(expected, result.get());
        verify(supplierReviewRepository).findById(1L);
    }

    @Test
    @DisplayName("handle(CheckIfBusinessmanReviewedSupplierQuery) debe delegar la validación de existencia (AAA)")
    void handle_CheckIfReviewed_ShouldDelegate() {
        // Arrange
        var query = mock(CheckIfBusinessmanReviewedSupplierQuery.class);
        when(query.supplierId()).thenReturn(10L);
        when(query.businessmanId()).thenReturn(20L);

        when(supplierReviewRepository.existsBySupplierIdAndBusinessmanId(new SupplierId(10L), new BusinessmanId(20L)))
                .thenReturn(true);

        // Act
        boolean result = service.handle(query);

        // Assert
        assertTrue(result);
        verify(supplierReviewRepository).existsBySupplierIdAndBusinessmanId(new SupplierId(10L), new BusinessmanId(20L));
    }
}