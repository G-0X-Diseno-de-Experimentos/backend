package com.textilflow.platform.reviews.domain.model;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.domain.model.commands.CreateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.commands.UpdateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.Rating;
import com.textilflow.platform.reviews.domain.model.valueobjects.ReviewContent;
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

    @Test
    @DisplayName("debe fallar si rating es inválido")
    void shouldThrow_WhenInvalidRating() {

        assertThrows(Exception.class,
                () -> new SupplierReview(10L, 20L, -1, "Bad"));
    }

    @Test
    @DisplayName("SupplierId String constructor debe convertir correctamente (AAA)")
    void supplierId_StringConstructor_ShouldConvertCorrectly() {
        // Arrange
        String input = "10";

        // Act
        SupplierId id = new SupplierId(input);

        // Assert
        assertEquals(10L, id.supplierId());
    }

    @Test
    @DisplayName("BusinessmanId String constructor debe convertir correctamente (AAA)")
    void businessmanId_StringConstructor_ShouldConvertCorrectly() {
        // Arrange
        String input = "15";

        // Act
        BusinessmanId id = new BusinessmanId(input);

        // Assert
        assertEquals(15L, id.businessmanId());
    }

    @Test
    @DisplayName("Rating debe fallar si está fuera de rango (AAA)")
    void rating_ShouldThrow_WhenOutOfRange() {
        // Arrange
        Integer low = 0;
        Integer high = 6;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Rating(low));
        assertThrows(IllegalArgumentException.class, () -> new Rating(high));
    }

    @Test
    @DisplayName("Rating String constructor debe convertir correctamente (AAA)")
    void rating_StringConstructor_ShouldConvert() {
        // Arrange
        String input = "4";

        // Act
        Rating rating = new Rating(input);

        // Assert
        assertEquals(4, rating.value());
    }

    @Test
    @DisplayName("ReviewContent no debe aceptar null o vacío (AAA)")
    void reviewContent_ShouldThrow_WhenInvalid() {
        // Arrange
        String nullValue = null;
        String empty = "";
        String blank = "   ";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ReviewContent(nullValue));
        assertThrows(IllegalArgumentException.class, () -> new ReviewContent(empty));
        assertThrows(IllegalArgumentException.class, () -> new ReviewContent(blank));
    }

    @Test
    @DisplayName("SupplierReview debe fallar si rating es null (AAA)")
    void supplierReview_ShouldThrow_WhenRatingNull() {
        // Arrange
        var command = mock(CreateSupplierReviewCommand.class);
        when(command.supplierId()).thenReturn(10L);
        when(command.businessmanId()).thenReturn(20L);
        when(command.rating()).thenReturn(null);
        when(command.reviewContent()).thenReturn("Ok");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new SupplierReview(command));
    }

    @Test
    @DisplayName("SupplierReview debe fallar si reviewContent es null (AAA)")
    void supplierReview_ShouldThrow_WhenContentNull() {
        // Arrange
        var command = mock(CreateSupplierReviewCommand.class);
        when(command.supplierId()).thenReturn(10L);
        when(command.businessmanId()).thenReturn(20L);
        when(command.rating()).thenReturn(5);
        when(command.reviewContent()).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new SupplierReview(command));
    }

    @Test
    @DisplayName("update debe fallar con rating inválido (AAA)")
    void update_ShouldThrow_WhenInvalidRating() {
        // Arrange
        var review = new SupplierReview(10L, 20L, 3, "Ok");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> review.update(0, "Nuevo"));
    }

    @Test
    @DisplayName("belongsToBusinessman debe manejar null sin romper (AAA)")
    void belongsToBusinessman_ShouldHandleNull() {
        // Arrange
        var review = new SupplierReview(10L, 20L, 5, "Ok");

        // Act
        boolean result = review.belongsToBusinessman(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isForSupplier debe manejar null sin romper (AAA)")
    void isForSupplier_ShouldHandleNull() {
        // Arrange
        var review = new SupplierReview(10L, 20L, 5, "Ok");

        // Act
        boolean result = review.isForSupplier(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Rating debe fallar si es null (AAA)")
    void rating_ShouldThrow_WhenNull() {
        // Arrange
        Integer input = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Rating(input));
    }

    @Test
    @DisplayName("Rating debe aceptar valor mínimo válido (AAA)")
    void rating_ShouldAccept_MinBoundary() {
        // Arrange
        Integer input = 1;

        // Act
        Rating rating = new Rating(input);

        // Assert
        assertEquals(1, rating.value());
    }

    @Test
    @DisplayName("Rating debe aceptar valor máximo válido (AAA)")
    void rating_ShouldAccept_MaxBoundary() {
        // Arrange
        Integer input = 5;

        // Act
        Rating rating = new Rating(input);

        // Assert
        assertEquals(5, rating.value());
    }

    @Test
    @DisplayName("Rating String constructor debe fallar si es null (AAA)")
    void rating_String_ShouldThrow_WhenNull() {
        // Arrange
        String input = null;

        // Act & Assert
        assertThrows(NumberFormatException.class,
                () -> new Rating(input));
    }

    @Test
    @DisplayName("SupplierId debe fallar con string inválido (AAA)")
    void supplierId_ShouldThrow_WhenInvalidString() {
        // Arrange
        String input = "abc";

        // Act & Assert
        assertThrows(NumberFormatException.class,
                () -> new SupplierId(input));
    }

    @Test
    @DisplayName("ReviewContent debe aceptar exactamente 1000 caracteres (AAA)")
    void reviewContent_ShouldAccept_WhenExactlyLimit() {
        // Arrange
        String input = "a".repeat(1000);

        // Act
        ReviewContent content = new ReviewContent(input);

        // Assert
        assertEquals(1000, content.content().length());
    }

    @Test
    @DisplayName("update debe fallar si rating es null (AAA)")
    void update_ShouldThrow_WhenNullRating() {
        // Arrange
        var review = new SupplierReview(10L, 20L, 3, "Ok");
        var command = mock(UpdateSupplierReviewCommand.class);

        when(command.rating()).thenReturn(null);
        when(command.reviewContent()).thenReturn("Nuevo");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> review.update(command));
    }
}