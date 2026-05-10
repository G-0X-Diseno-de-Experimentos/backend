package com.textilflow.platform.reviews.application.internal.eventhandlers;

import com.textilflow.platform.reviews.domain.model.events.ReviewCreatedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReviewCreatedEventHandlerTest {

    @Test
    @DisplayName("on(ReviewCreatedEvent) debe procesar y loguear exitosamente sin lanzar excepciones (AAA)")
    void on_ReviewCreatedEvent_ShouldProcessSuccessfully() {
        // Arrange
        var event = mock(ReviewCreatedEvent.class);
        when(event.getReviewId()).thenReturn(1L);
        when(event.getSupplierIdValue()).thenReturn(10L);
        when(event.getBusinessmanIdValue()).thenReturn(20L);
        when(event.getRatingValue()).thenReturn(5);

        var handler = new ReviewCreatedEventHandler();

        // Act & Assert
        assertDoesNotThrow(() -> handler.on(event));
    }

    @Test
    @DisplayName("on(ReviewCreatedEvent) debe capturar excepciones internas de forma segura (AAA)")
    void on_ReviewCreatedEvent_ShouldHandleInternalExceptionsSafely() {
        // Arrange
        var event = mock(ReviewCreatedEvent.class);
        when(event.getReviewId()).thenThrow(new RuntimeException("Unexpected internal failure"));

        var handler = new ReviewCreatedEventHandler();

        // Act & Assert
        assertDoesNotThrow(() -> handler.on(event));
    }
}