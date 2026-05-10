package com.textilflow.platform.reviews.application.internal.commandservices;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.domain.model.commands.CreateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.commands.UpdateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.SupplierId;
import com.textilflow.platform.reviews.infrastructure.persistence.jpa.repositories.SupplierReviewRepository;
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
class SupplierReviewCommandServiceImplTest {

    @Mock
    private SupplierReviewRepository supplierReviewRepository;

    @InjectMocks
    private SupplierReviewCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateSupplierReviewCommand) debe lanzar excepción si ya existe reseña (AAA)")
    void handle_CreateReview_ShouldThrow_WhenReviewAlreadyExists() {
        // Arrange
        var command = mock(CreateSupplierReviewCommand.class);
        when(command.supplierId()).thenReturn(10L);
        when(command.businessmanId()).thenReturn(20L);

        when(supplierReviewRepository.existsBySupplierIdAndBusinessmanId(new SupplierId(10L), new BusinessmanId(20L)))
                .thenReturn(true);

        // Act + Assert
        var ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("has already reviewed supplier"));
        verify(supplierReviewRepository).existsBySupplierIdAndBusinessmanId(new SupplierId(10L), new BusinessmanId(20L));
        verifyNoMoreInteractions(supplierReviewRepository);
    }

    @Test
    @DisplayName("handle(CreateSupplierReviewCommand) debe guardar exitosamente si no existe previa (AAA)")
    void handle_CreateReview_ShouldSaveSuccessfully() {
        // Arrange
        var command = mock(CreateSupplierReviewCommand.class);
        when(command.supplierId()).thenReturn(10L);
        when(command.businessmanId()).thenReturn(20L);
        when(command.rating()).thenReturn(5);
        when(command.reviewContent()).thenReturn("Excelente servicio");

        when(supplierReviewRepository.existsBySupplierIdAndBusinessmanId(new SupplierId(10L), new BusinessmanId(20L)))
                .thenReturn(false);

        SupplierReview savedMock = mock(SupplierReview.class);
        when(supplierReviewRepository.save(any(SupplierReview.class))).thenReturn(savedMock);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(savedMock, result.get());
        verify(supplierReviewRepository).existsBySupplierIdAndBusinessmanId(new SupplierId(10L), new BusinessmanId(20L));
        verify(supplierReviewRepository).save(any(SupplierReview.class));
    }

    @Test
    @DisplayName("handle(CreateSupplierReviewCommand) debe lanzar RuntimeException si falla la persistencia (AAA)")
    void handle_CreateReview_ShouldThrowRuntimeException_OnSaveError() {
        // Arrange
        var command = mock(CreateSupplierReviewCommand.class);
        when(command.supplierId()).thenReturn(10L);
        when(command.businessmanId()).thenReturn(20L);
        when(command.rating()).thenReturn(5);
        when(command.reviewContent()).thenReturn("Excelente servicio");

        when(supplierReviewRepository.existsBySupplierIdAndBusinessmanId(new SupplierId(10L), new BusinessmanId(20L)))
                .thenReturn(false);

        when(supplierReviewRepository.save(any(SupplierReview.class))).thenThrow(new RuntimeException("DB Error"));

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Error saving supplier review"));
    }

    @Test
    @DisplayName("handle(UpdateSupplierReviewCommand) debe lanzar excepción si la reseña no existe (AAA)")
    void handle_UpdateReview_ShouldThrow_WhenNotFound() {
        // Arrange
        var command = mock(UpdateSupplierReviewCommand.class);
        when(command.reviewId()).thenReturn(1L);
        when(supplierReviewRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        var ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("not found"));
        verify(supplierReviewRepository).findById(1L);
        verifyNoMoreInteractions(supplierReviewRepository);
    }

    @Test
    @DisplayName("handle(UpdateSupplierReviewCommand) debe actualizar y persistir exitosamente (AAA)")
    void handle_UpdateReview_ShouldUpdateSuccessfully() {
        // Arrange
        var command = mock(UpdateSupplierReviewCommand.class);
        when(command.reviewId()).thenReturn(1L);
        when(command.rating()).thenReturn(4);
        when(command.reviewContent()).thenReturn("Buen servicio actualizado");

        SupplierReview existingReview = spy(new SupplierReview(10L, 20L, 3, "Antiguo"));
        when(supplierReviewRepository.findById(1L)).thenReturn(Optional.of(existingReview));
        when(supplierReviewRepository.save(existingReview)).thenReturn(existingReview);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        verify(supplierReviewRepository).findById(1L);
        verify(existingReview).update(command);
        verify(supplierReviewRepository).save(existingReview);
    }
}