package com.textilflow.platform.batches.domain.model;

import com.textilflow.platform.batches.domain.model.aggregates.Batch;
import com.textilflow.platform.batches.domain.model.commands.*;
import com.textilflow.platform.batches.domain.model.events.BatchCreatedEvent;
import com.textilflow.platform.batches.domain.model.events.BatchUpdatedEvent;
import com.textilflow.platform.batches.domain.model.queries.GetBatchByIdQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchesByBusinessmanIdQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchesBySupplierIdQuery;
import com.textilflow.platform.batches.domain.model.valueobjects.BatchStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BatchDomainContractsTest {

    @Test
    @DisplayName("Batch constructor should set default values (AAA)")
    void batch_Constructor_ShouldSetDefaults() {
        // Arrange & Act
        Batch batch = new Batch();

        // Assert
        assertEquals("", batch.getCode());
        assertEquals("", batch.getClient());
        assertEquals("", batch.getFabricType());
        assertEquals("", batch.getColor());
        assertEquals(0, batch.getQuantity());
        assertEquals(0.0, batch.getPrice());
        assertEquals("", batch.getObservations());
        assertEquals("", batch.getAddress());
        assertNotNull(batch.getDate());
        assertEquals(BatchStatus.PENDIENTE, batch.getStatus());
        assertNull(batch.getImageUrl());
    }

    @Test
    @DisplayName("Batch constructor from CreateBatchCommand should map values (AAA)")
    void batch_ConstructorFromCommand_ShouldMapValues() {
        // Arrange
        var command = new CreateBatchCommand("C01", "Client", 1L, 2L, "Type", "Color", 10.0, 5, "Obs", "Addr", LocalDate.now(), BatchStatus.ACEPTADO, "url");

        // Act
        Batch batch = new Batch(command);

        // Assert
        assertEquals("C01", batch.getCode());
        assertEquals("Client", batch.getClient());
        assertEquals(1L, batch.getBusinessmanId());
        assertEquals(2L, batch.getSupplierId());
        assertEquals("Type", batch.getFabricType());
        assertEquals("Color", batch.getColor());
        assertEquals(10.0, batch.getPrice());
        assertEquals(5, batch.getQuantity());
        assertEquals("Obs", batch.getObservations());
        assertEquals("Addr", batch.getAddress());
        assertEquals(BatchStatus.ACEPTADO, batch.getStatus());
        assertEquals("url", batch.getImageUrl());
    }

    @Test
    @DisplayName("CreateBatchCommand validations should throw on bad inputs (AAA)")
    void createBatchCommand_Validations() {
        // Arrange
        LocalDate pastDate = LocalDate.now();
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand(null, "Client", 1L, 2L, "Type", "Color", 10.0, 5, "Obs", "Addr", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", null, 1L, 2L, "Type", "Color", 10.0, 5, "Obs", "Addr", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 0L, 2L, "Type", "Color", 10.0, 5, "Obs", "Addr", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 1L, 0L, "Type", "Color", 10.0, 5, "Obs", "Addr", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 1L, 2L, "", "Color", 10.0, 5, "Obs", "Addr", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 1L, 2L, "Type", "", 10.0, 5, "Obs", "Addr", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 1L, 2L, "Type", "Color", 0.0, 5, "Obs", "Addr", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 1L, 2L, "Type", "Color", 10.0, 0, "Obs", "Addr", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 1L, 2L, "Type", "Color", 10.0, 5, "", "Addr", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 1L, 2L, "Type", "Color", 10.0, 5, "Obs", "", pastDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 1L, 2L, "Type", "Color", 10.0, 5, "Obs", "Addr", futureDate, BatchStatus.PENDIENTE, "url"));
        assertThrows(IllegalArgumentException.class, () -> new CreateBatchCommand("C01", "Client", 1L, 2L, "Type", "Color", 10.0, 5, "Obs", "Addr", pastDate, null, "url"));
    }

    @Test
    @DisplayName("UpdateBatchCommand validations should throw on bad inputs (AAA)")
    void updateBatchCommand_Validations() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchCommand(0L, "C", "Cl", 1L, 2L, "T", "Co", 5, 10.0, "O", "A", LocalDate.now(), BatchStatus.PENDIENTE, "u"));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchCommand(1L, "", "Cl", 1L, 2L, "T", "Co", 5, 10.0, "O", "A", LocalDate.now(), BatchStatus.PENDIENTE, "u"));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchCommand(1L, "C", "", 1L, 2L, "T", "Co", 5, 10.0, "O", "A", LocalDate.now(), BatchStatus.PENDIENTE, "u"));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchCommand(1L, "C", "Cl", 0L, 2L, "T", "Co", 5, 10.0, "O", "A", LocalDate.now(), BatchStatus.PENDIENTE, "u"));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchCommand(1L, "C", "Cl", 1L, 0L, "T", "Co", 5, 10.0, "O", "A", LocalDate.now(), BatchStatus.PENDIENTE, "u"));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchCommand(1L, "C", "Cl", 1L, 2L, "T", "Co", 0, 10.0, "O", "A", LocalDate.now(), BatchStatus.PENDIENTE, "u"));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchCommand(1L, "C", "Cl", 1L, 2L, "T", "Co", 5, -1.0, "O", "A", LocalDate.now(), BatchStatus.PENDIENTE, "u"));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchCommand(1L, "C", "Cl", 1L, 2L, "T", "Co", 5, 10.0, "O", "A", null, BatchStatus.PENDIENTE, "u"));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchCommand(1L, "C", "Cl", 1L, 2L, "T", "Co", 5, 10.0, "O", "A", LocalDate.now(), null, "u"));
    }

    @Test
    @DisplayName("Delete commands validations should throw on bad batchId (AAA)")
    void deleteCommands_Validations() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new DeleteBatchCommand(0L));
        assertThrows(IllegalArgumentException.class, () -> new DeleteBatchImageCommand(0L));
    }

    @Test
    @DisplayName("UpdateBatchImageCommand validations should throw on bad inputs (AAA)")
    void updateBatchImageCommand_Validations() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchImageCommand(0L, "url"));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchImageCommand(1L, null));
        assertThrows(IllegalArgumentException.class, () -> new UpdateBatchImageCommand(1L, ""));
    }

    @Test
    @DisplayName("Queries validations should throw on bad IDs (AAA)")
    void queries_Validations() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new GetBatchByIdQuery(0L));
        assertThrows(IllegalArgumentException.class, () -> new GetBatchesByBusinessmanIdQuery(0L));
        assertThrows(IllegalArgumentException.class, () -> new GetBatchesBySupplierIdQuery(0L));
    }

    @Test
    @DisplayName("BatchStatus should return correct display name (AAA)")
    void batchStatus_DisplayName() {
        // Arrange & Act & Assert
        assertEquals("Pendiente", BatchStatus.PENDIENTE.getDisplayName());
        assertEquals("Aceptado", BatchStatus.ACEPTADO.getDisplayName());
    }

    @Test
    @DisplayName("Events records coverage (AAA)")
    void events_ContractCoverage() {
        // Arrange & Act
        var created = new BatchCreatedEvent(1L, "C", "Cl", 1L, 2L, "T", "Co", 5, 10.0, "O", "A", LocalDate.now(), BatchStatus.PENDIENTE, "u");
        var updated = new BatchUpdatedEvent(1L, "C", "Cl", 1L, 2L, "T", "Co", 5, 10.0, "O", "A", LocalDate.now(), BatchStatus.PENDIENTE, "u");

        // Assert
        assertEquals(1L, created.batchId());
        assertEquals(1L, updated.batchId());
    }

    @Test
    @DisplayName("updateInformation debe actualizar todos los campos y retornar la misma instancia (AAA)")
    void updateInformation_ShouldUpdateAllFieldsAndReturnSameInstance() {
        // Arrange
        Batch batch = new Batch();

        LocalDate date = LocalDate.now();

        // Act
        Batch result = batch.updateInformation(
                "C100",
                "ClientX",
                10L,
                20L,
                "Cotton",
                "Blue",
                100,
                2500.0,
                "Obs",
                "Lima",
                date,
                BatchStatus.ENVIADO,
                "http://img.png"
        );

        // Assert
        assertSame(batch, result);

        assertEquals("C100", batch.getCode());
        assertEquals("ClientX", batch.getClient());
        assertEquals(10L, batch.getBusinessmanId());
        assertEquals(20L, batch.getSupplierId());
        assertEquals("Cotton", batch.getFabricType());
        assertEquals("Blue", batch.getColor());
        assertEquals(100, batch.getQuantity());
        assertEquals(2500.0, batch.getPrice());
        assertEquals("Obs", batch.getObservations());
        assertEquals("Lima", batch.getAddress());
        assertEquals(date, batch.getDate());
        assertEquals(BatchStatus.ENVIADO, batch.getStatus());
        assertEquals("http://img.png", batch.getImageUrl());
    }

    @Test
    @DisplayName("updateInformation debe permitir imageUrl null sin fallar (edge case)")
    void updateInformation_ShouldAllowNullImageUrl() {
        // Arrange
        Batch batch = new Batch();

        // Act
        batch.updateInformation(
                "C01",
                "Client",
                1L,
                2L,
                "Cotton",
                "Red",
                10,
                100.0,
                "Obs",
                "Address",
                LocalDate.now(),
                BatchStatus.PENDIENTE,
                null
        );

        // Assert
        assertNull(batch.getImageUrl());
        assertEquals("C01", batch.getCode());

    }

    @Test
    @DisplayName("updateInformation debe aceptar strings vacíos (edge case de dominio flexible)")
    void updateInformation_ShouldAllowEmptyStrings() {
        // Arrange
        Batch batch = new Batch();

        // Act
        batch.updateInformation(
                "",
                "",
                1L,
                2L,
                "",
                "",
                0,
                0.0,
                "",
                "",
                LocalDate.now(),
                BatchStatus.PENDIENTE,
                ""
        );

        // Assert
        assertEquals("", batch.getCode());
        assertEquals("", batch.getClient());
        assertEquals("", batch.getFabricType());
        assertEquals("", batch.getColor());
        assertEquals("", batch.getObservations());
        assertEquals("", batch.getAddress());
        assertEquals("", batch.getImageUrl());
    }

    @Test
    @DisplayName("constructor desde command debe fallar si status es null (edge case)")
    void constructorFromCommand_ShouldHandleNullStatus() {
        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new CreateBatchCommand(
                        "C01",
                        "Client",
                        1L,
                        2L,
                        "Type",
                        "Color",
                        10.0,
                        5,
                        "Obs",
                        "Addr",
                        LocalDate.now(),
                        null,
                        "url"
                )
        );

        assertTrue(ex.getMessage().contains("Status"));
    }

    @Test
    @DisplayName("updateInformation debe actualizar fecha correctamente incluso con misma referencia (edge case)")
    void updateInformation_ShouldReplaceDateReference() {
        // Arrange
        Batch batch = new Batch();
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.plusDays(1);

        // Act
        batch.updateInformation(
                "C01", "Client", 1L, 2L,
                "T", "C", 1, 10.0,
                "O", "A",
                date1,
                BatchStatus.PENDIENTE,
                null
        );

        batch.updateInformation(
                "C01", "Client", 1L, 2L,
                "T", "C", 1, 10.0,
                "O", "A",
                date2,
                BatchStatus.PENDIENTE,
                null
        );

        // Assert
        assertEquals(date2, batch.getDate());
    }
}