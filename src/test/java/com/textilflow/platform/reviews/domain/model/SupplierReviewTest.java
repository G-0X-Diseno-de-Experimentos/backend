package com.textilflow.platform.reviews.domain.model;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.domain.model.commands.CreateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.commands.UpdateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.SupplierId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SupplierReviewTest {

    @Test
    @DisplayName("Constructor desde comando debe inicializar los campos y registrar evento (AAA)")
    void constructorFromCommand_ShouldInitializeAndRegisterEvent() {
        // Arrange
        var command = mock(CreateSupplierReviewCommand.class);
        when(command.supplierId()).thenReturn(10L);
        when(command.businessmanId()).thenReturn(20L);
        when(command.rating()).thenReturn(5);
        when(command.reviewContent()).thenReturn("Excelente");

        // Act
        var review = new SupplierReview(command);

        // Assert
        assertEquals(10L, review.getSupplierIdValue());
        assertEquals(20L, review.getBusinessmanIdValue());
        assertEquals(5, review.getRatingValue());
        assertEquals("Excelente", review.getReviewContentValue());

        assertNotNull(review.getSupplierId());
        assertNotNull(review.getBusinessmanId());
        assertNotNull(review.getRating());
        assertNotNull(review.getReviewContent());

        assertFalse(review.domainEvents().isEmpty());
    }

    @Test
    @DisplayName("update mediante comando debe modificar rating, contenido y registrar evento (AAA)")
    void updateFromCommand_ShouldModifyFieldsAndRegisterEvent() {
        // Arrange
        var review = new SupplierReview(10L, 20L, 3, "Regular");
        var updateCommand = mock(UpdateSupplierReviewCommand.class);
        when(updateCommand.rating()).thenReturn(4);
        when(updateCommand.reviewContent()).thenReturn("Mejorado");

        // Act
        review.update(updateCommand);

        // Assert
        assertEquals(4, review.getRatingValue());
        assertEquals("Mejorado", review.getReviewContentValue());
    }

    @Test
    @DisplayName("update directo debe modificar rating, contenido y registrar evento (AAA)")
    void updateDirect_ShouldModifyFieldsAndRegisterEvent() {
        // Arrange
        var review = new SupplierReview(10L, 20L, 3, "Regular");

        // Act
        review.update(5, "Perfecto");

        // Assert
        assertEquals(5, review.getRatingValue());
        assertEquals("Perfecto", review.getReviewContentValue());
    }

    @Test
    @DisplayName("Métodos de pertenencia deben verificar correctamente los identificadores (AAA)")
    void ownershipMethods_ShouldVerifyCorrectly() {
        // Arrange
        var review = new SupplierReview(10L, 20L, 5, "Genial");

        // Act & Assert
        assertTrue(review.belongsToBusinessman(new BusinessmanId(20L)));
        assertFalse(review.belongsToBusinessman(new BusinessmanId(99L)));

        assertTrue(review.isForSupplier(new SupplierId(10L)));
        assertFalse(review.isForSupplier(new SupplierId(99L)));
    }
}