package com.textilflow.platform.observation.domain.model;

import com.textilflow.platform.observation.domain.model.aggregates.Observation;
import com.textilflow.platform.observation.domain.model.commands.UpdateObservationCommand;
import com.textilflow.platform.observation.domain.model.valueobjects.BatchCode;
import com.textilflow.platform.observation.domain.model.valueobjects.ImageUrl;
import com.textilflow.platform.observation.domain.model.valueobjects.ObservationStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObservationDomainContractsTest {

    @Test
    @DisplayName("Constructor vacío debe inicializar sin errores (AAA)")
    void emptyConstructor_ShouldInitialize() {
        // Arrange & Act
        Observation observation = new Observation();

        // Assert
        assertNull(observation.getBatchId());
        assertNull(observation.getBatchCodeValue());
        assertNull(observation.getImageUrlValue());
        assertNull(observation.getStatusValue());
    }

    @Test
    @DisplayName("Constructor completo debe asignar valores y permitir lectura segura mediante getters (AAA)")
    void fullConstructor_ShouldAssignValues() {
        // Arrange
        BatchCode batchCode = new BatchCode("B001");
        ImageUrl imageUrl = new ImageUrl("http://url/img.jpg");

        // Act
        Observation observation = new Observation(10L, batchCode, 100L, 200L, "Falla", imageUrl, ObservationStatus.PENDIENTE);

        // Assert
        assertEquals(10L, observation.getBatchId());
        assertEquals("B001", observation.getBatchCodeValue());
        assertEquals(100L, observation.getBusinessmanId());
        assertEquals(200L, observation.getSupplierId());
        assertEquals("Falla", observation.getReason());
        assertEquals("http://url/img.jpg", observation.getImageUrlValue());
        assertEquals("PENDIENTE", observation.getStatusValue());
        assertEquals(ObservationStatus.PENDIENTE, observation.getStatus());
    }

    @Test
    @DisplayName("updateInformation debe modificar la razón, estado e imagen correctamente (AAA)")
    void updateInformation_ShouldModifyFields() {
        // Arrange
        Observation observation = new Observation(10L, new BatchCode("B001"), 100L, 200L, "Vieja razón", null, ObservationStatus.PENDIENTE);
        var command = new UpdateObservationCommand(1L, "Nueva razón", "http://new/img.jpg", "RESUELTA");

        // Act
        observation.updateInformation(command);

        // Assert
        assertEquals("Nueva razón", observation.getReason());
        assertEquals("RESUELTA", observation.getStatusValue());
        assertEquals("http://new/img.jpg", observation.getImageUrlValue());
    }

    @Test
    @DisplayName("updateImage y deleteImage deben gestionar el ciclo de vida del Value Object (AAA)")
    void imageLifecycle_Management() {
        // Arrange
        Observation observation = new Observation();

        // Act
        observation.updateImage("http://cloud/img.jpg");
        // Assert
        assertEquals("http://cloud/img.jpg", observation.getImageUrlValue());

        // Act
        observation.deleteImage();
        // Assert
        assertNull(observation.getImageUrlValue());
    }

    @Test
    @DisplayName("Enum ObservationStatus debe contener los valores de negocio definidos (AAA)")
    void observationStatus_EnumCheck() {
        // Arrange & Act & Assert
        assertNotNull(ObservationStatus.valueOf("PENDIENTE"));
        assertNotNull(ObservationStatus.valueOf("EN_REVISION"));
        assertNotNull(ObservationStatus.valueOf("RESUELTA"));
        assertNotNull(ObservationStatus.valueOf("RECHAZADA"));
    }

    @Test
    @DisplayName("updateImage debe manejar null (edge case)")
    void updateImage_ShouldHandleNull() {
        // Arrange
        Observation observation = new Observation();

        // Act
        observation.updateImage(null);

        // Assert
        assertNull(observation.getImageUrlValue());
    }

    @Test
    @DisplayName("ImageUrl isEmpty debe detectar null y vacío")
    void imageUrl_IsEmptyEdgeCases() {
        // Arrange
        ImageUrl nullUrl = new ImageUrl(null);
        ImageUrl emptyUrl = new ImageUrl("");
        ImageUrl spacesUrl = new ImageUrl("   ");
        ImageUrl validUrl = new ImageUrl("http://test.com");

        // Assert
        assertTrue(nullUrl.isEmpty());
        assertTrue(emptyUrl.isEmpty());
        assertTrue(spacesUrl.isEmpty());
        assertFalse(validUrl.isEmpty());
    }

    @Test
    @DisplayName("updateInformation no debe cambiar imagen si es null o vacía")
    void updateInformation_ShouldIgnoreNullImage() {
        // Arrange
        Observation observation = new Observation(
                1L,
                new BatchCode("B001"),
                100L,
                200L,
                "Reason",
                new ImageUrl("http://old.com"),
                ObservationStatus.PENDIENTE
        );

        var command = new UpdateObservationCommand(
                1L,
                "New Reason",
                null,
                "RESUELTA"
        );

        // Act
        observation.updateInformation(command);

        // Assert
        assertEquals("http://old.com", observation.getImageUrlValue());
    }

    @Test
    @DisplayName("updateInformation debe fallar si status es inválido")
    void updateInformation_ShouldFailInvalidStatus() {
        // Arrange
        Observation observation = new Observation();

        var command = new UpdateObservationCommand(
                1L,
                "Reason",
                "http://img.com",
                "INVALID_STATUS"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                observation.updateInformation(command)
        );
    }

    @Test
    @DisplayName("Constructor debe aceptar imageUrl null pero mantener consistencia")
    void constructor_ShouldAllowNullImageUrl() {
        // Arrange
        BatchCode batchCode = new BatchCode("B001");

        // Act
        Observation observation = new Observation(
                1L,
                batchCode,
                100L,
                200L,
                "Reason",
                null,
                ObservationStatus.PENDIENTE
        );

        // Assert
        assertNull(observation.getImageUrlValue());
        assertEquals("B001", observation.getBatchCodeValue());
    }
}