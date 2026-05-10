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
}