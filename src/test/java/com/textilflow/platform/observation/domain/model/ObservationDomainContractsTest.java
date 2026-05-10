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
        var command = new UpdateObservationCommand(1L, "Nueva razón", "RESUELTA", "http://new/img.jpg");

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
}