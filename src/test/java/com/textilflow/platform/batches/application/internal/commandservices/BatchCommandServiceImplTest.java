package com.textilflow.platform.batches.application.internal.commandservices;

import com.textilflow.platform.batches.domain.model.aggregates.Batch;
import com.textilflow.platform.batches.domain.model.commands.*;
import com.textilflow.platform.batches.domain.model.events.BatchCreatedEvent;
import com.textilflow.platform.batches.domain.model.events.BatchUpdatedEvent;
import com.textilflow.platform.batches.domain.model.valueobjects.BatchStatus;
import com.textilflow.platform.batches.infraestructure.persistence.repositories.BatchRepository;
import com.textilflow.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchCommandServiceImplTest {

    @Mock private BatchRepository batchRepository;
    @Mock private ProfilesContextFacade profilesContextFacade;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks private BatchCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateBatchCommand) should create batch when ok (AAA)")
    void handle_CreateBatch_ShouldCreate_WhenOk() {
        // Arrange
        var command = new CreateBatchCommand("B001", "Client A", 10L, 20L, "Cotton", "Blue", 150.0, 50, "Obs", "Address", LocalDate.now(), BatchStatus.PENDIENTE, "http://image.url");
        when(batchRepository.existsByCode("B001")).thenReturn(false);
        when(profilesContextFacade.getBusinessmanByUserId(10L)).thenReturn(100L);
        when(profilesContextFacade.getSupplierByUserId(20L)).thenReturn(200L);

        Batch savedBatchMock = mock(Batch.class);
        when(savedBatchMock.getId()).thenReturn(1L);
        when(batchRepository.save(any(Batch.class))).thenReturn(savedBatchMock);

        // Act
        Long batchId = service.handle(command);

        // Assert
        assertEquals(1L, batchId);
        verify(batchRepository).existsByCode("B001");
        verify(profilesContextFacade).getBusinessmanByUserId(10L);
        verify(profilesContextFacade).getSupplierByUserId(20L);
        verify(batchRepository).save(any(Batch.class));
        verify(eventPublisher).publishEvent(any(BatchCreatedEvent.class));
        verifyNoMoreInteractions(batchRepository, profilesContextFacade, eventPublisher);
    }

    @Test
    @DisplayName("handle(CreateBatchCommand) should throw when code already exists (AAA)")
    void handle_CreateBatch_ShouldThrow_WhenCodeExists() {
        // Arrange
        var command = new CreateBatchCommand("B001", "Client A", 10L, 20L, "Cotton", "Blue", 150.0, 50, "Obs", "Address", LocalDate.now(), BatchStatus.PENDIENTE, "http://image.url");
        when(batchRepository.existsByCode("B001")).thenReturn(true);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Batch with code already exists"));
        verify(batchRepository).existsByCode("B001");
        verifyNoMoreInteractions(batchRepository);
        verifyNoInteractions(profilesContextFacade, eventPublisher);
    }

    @Test
    @DisplayName("handle(CreateBatchCommand) should throw when businessman not found (AAA)")
    void handle_CreateBatch_ShouldThrow_WhenBusinessmanNotFound() {
        // Arrange
        var command = new CreateBatchCommand("B001", "Client A", 10L, 20L, "Cotton", "Blue", 150.0, 50, "Obs", "Address", LocalDate.now(), BatchStatus.PENDIENTE, "http://image.url");
        when(batchRepository.existsByCode("B001")).thenReturn(false);
        when(profilesContextFacade.getBusinessmanByUserId(10L)).thenReturn(null);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Businessman not found"));
        verify(batchRepository).existsByCode("B001");
        verify(profilesContextFacade).getBusinessmanByUserId(10L);
        verifyNoMoreInteractions(batchRepository, profilesContextFacade);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("handle(CreateBatchCommand) should throw when supplier not found (AAA)")
    void handle_CreateBatch_ShouldThrow_WhenSupplierNotFound() {
        // Arrange
        var command = new CreateBatchCommand("B001", "Client A", 10L, 20L, "Cotton", "Blue", 150.0, 50, "Obs", "Address", LocalDate.now(), BatchStatus.PENDIENTE, "http://image.url");
        when(batchRepository.existsByCode("B001")).thenReturn(false);
        when(profilesContextFacade.getBusinessmanByUserId(10L)).thenReturn(100L);
        when(profilesContextFacade.getSupplierByUserId(20L)).thenReturn(null);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Supplier not found"));
        verify(batchRepository).existsByCode("B001");
        verify(profilesContextFacade).getBusinessmanByUserId(10L);
        verify(profilesContextFacade).getSupplierByUserId(20L);
        verifyNoMoreInteractions(batchRepository, profilesContextFacade);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateBatchCommand) should update batch when ok (AAA)")
    void handle_UpdateBatch_ShouldUpdate_WhenOk() {
        // Arrange
        var command = new UpdateBatchCommand(1L, "B002", "Client B", 10L, 20L, "Silk", "Red", 100, 200.0, "New Obs", "New Address", LocalDate.now(), BatchStatus.ACEPTADO, "http://new.url");
        Batch existingBatch = spy(new Batch());
        when(batchRepository.findById(1L)).thenReturn(Optional.of(existingBatch));
        when(batchRepository.existsByCodeAndIdIsNot("B002", 1L)).thenReturn(false);
        when(profilesContextFacade.getBusinessmanByUserId(10L)).thenReturn(100L);
        when(profilesContextFacade.getSupplierByUserId(20L)).thenReturn(200L);
        when(existingBatch.getId()).thenReturn(1L);
        when(batchRepository.save(existingBatch)).thenReturn(existingBatch);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("B002", result.get().getCode());
        verify(batchRepository).findById(1L);
        verify(batchRepository).existsByCodeAndIdIsNot("B002", 1L);
        verify(profilesContextFacade).getBusinessmanByUserId(10L);
        verify(profilesContextFacade).getSupplierByUserId(20L);
        verify(batchRepository).save(existingBatch);
        verify(eventPublisher).publishEvent(any(BatchUpdatedEvent.class));
        verifyNoMoreInteractions(batchRepository, profilesContextFacade, eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateBatchCommand) should throw when batch not found (AAA)")
    void handle_UpdateBatch_ShouldThrow_WhenNotFound() {
        // Arrange
        var command = new UpdateBatchCommand(1L, "B002", "Client B", 10L, 20L, "Silk", "Red", 100, 200.0, "New Obs", "New Address", LocalDate.now(), BatchStatus.ACEPTADO, "http://new.url");
        when(batchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Batch not found"));
        verify(batchRepository).findById(1L);
        verifyNoMoreInteractions(batchRepository);
        verifyNoInteractions(profilesContextFacade, eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateBatchCommand) should throw when another batch has same code (AAA)")
    void handle_UpdateBatch_ShouldThrow_WhenAnotherHasSameCode() {
        // Arrange
        var command = new UpdateBatchCommand(1L, "B002", "Client B", 10L, 20L, "Silk", "Red", 100, 200.0, "New Obs", "New Address", LocalDate.now(), BatchStatus.ACEPTADO, "http://new.url");
        when(batchRepository.findById(1L)).thenReturn(Optional.of(new Batch()));
        when(batchRepository.existsByCodeAndIdIsNot("B002", 1L)).thenReturn(true);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Another batch with this code already exists"));
        verify(batchRepository).findById(1L);
        verify(batchRepository).existsByCodeAndIdIsNot("B002", 1L);
        verifyNoMoreInteractions(batchRepository);
        verifyNoInteractions(profilesContextFacade, eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateBatchCommand) should throw when businessman not found (AAA)")
    void handle_UpdateBatch_ShouldThrow_WhenBusinessmanNotFound() {
        // Arrange
        var command = new UpdateBatchCommand(1L, "B002", "Client B", 10L, 20L, "Silk", "Red", 100, 200.0, "New Obs", "New Address", LocalDate.now(), BatchStatus.ACEPTADO, "http://new.url");
        when(batchRepository.findById(1L)).thenReturn(Optional.of(new Batch()));
        when(batchRepository.existsByCodeAndIdIsNot("B002", 1L)).thenReturn(false);
        when(profilesContextFacade.getBusinessmanByUserId(10L)).thenReturn(null);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Businessman not found"));
        verify(batchRepository).findById(1L);
        verify(batchRepository).existsByCodeAndIdIsNot("B002", 1L);
        verify(profilesContextFacade).getBusinessmanByUserId(10L);
        verifyNoMoreInteractions(batchRepository, profilesContextFacade);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateBatchCommand) should throw when supplier not found (AAA)")
    void handle_UpdateBatch_ShouldThrow_WhenSupplierNotFound() {
        // Arrange
        var command = new UpdateBatchCommand(1L, "B002", "Client B", 10L, 20L, "Silk", "Red", 100, 200.0, "New Obs", "New Address", LocalDate.now(), BatchStatus.ACEPTADO, "http://new.url");
        when(batchRepository.findById(1L)).thenReturn(Optional.of(new Batch()));
        when(batchRepository.existsByCodeAndIdIsNot("B002", 1L)).thenReturn(false);
        when(profilesContextFacade.getBusinessmanByUserId(10L)).thenReturn(100L);
        when(profilesContextFacade.getSupplierByUserId(20L)).thenReturn(null);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Supplier not found"));
        verify(batchRepository).findById(1L);
        verify(batchRepository).existsByCodeAndIdIsNot("B002", 1L);
        verify(profilesContextFacade).getBusinessmanByUserId(10L);
        verify(profilesContextFacade).getSupplierByUserId(20L);
        verifyNoMoreInteractions(batchRepository, profilesContextFacade);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("handle(DeleteBatchCommand) should delete when ok (AAA)")
    void handle_DeleteBatch_ShouldDelete_WhenOk() {
        // Arrange
        var command = new DeleteBatchCommand(1L);
        when(batchRepository.existsById(1L)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(batchRepository).existsById(1L);
        verify(batchRepository).deleteById(1L);
        verifyNoMoreInteractions(batchRepository);
    }

    @Test
    @DisplayName("handle(DeleteBatchCommand) should throw when not found (AAA)")
    void handle_DeleteBatch_ShouldThrow_WhenNotFound() {
        // Arrange
        var command = new DeleteBatchCommand(1L);
        when(batchRepository.existsById(1L)).thenReturn(false);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Batch not found"));
        verify(batchRepository).existsById(1L);
        verifyNoMoreInteractions(batchRepository);
    }

    @Test
    @DisplayName("handle(UpdateBatchImageCommand) should update image when ok (AAA)")
    void handle_UpdateBatchImage_ShouldUpdate_WhenOk() {
        // Arrange
        var command = new UpdateBatchImageCommand(1L, "http://new-image.url");
        Batch batch = spy(new Batch());
        when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));
        when(batchRepository.save(batch)).thenReturn(batch);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("http://new-image.url", result.get().getImageUrl());
        verify(batchRepository).findById(1L);
        verify(batchRepository).save(batch);
        verifyNoMoreInteractions(batchRepository);
    }

    @Test
    @DisplayName("handle(UpdateBatchImageCommand) should throw when not found (AAA)")
    void handle_UpdateBatchImage_ShouldThrow_WhenNotFound() {
        // Arrange
        var command = new UpdateBatchImageCommand(1L, "http://new-image.url");
        when(batchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Batch not found"));
        verify(batchRepository).findById(1L);
        verifyNoMoreInteractions(batchRepository);
    }

    @Test
    @DisplayName("handle(DeleteBatchImageCommand) should clear image when ok (AAA)")
    void handle_DeleteBatchImage_ShouldClear_WhenOk() {
        // Arrange
        var command = new DeleteBatchImageCommand(1L);
        Batch batch = spy(new Batch());
        when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));
        when(batchRepository.save(batch)).thenReturn(batch);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("", result.get().getImageUrl());
        verify(batchRepository).findById(1L);
        verify(batchRepository).save(batch);
        verifyNoMoreInteractions(batchRepository);
    }

    @Test
    @DisplayName("handle(DeleteBatchImageCommand) should throw when not found (AAA)")
    void handle_DeleteBatchImage_ShouldThrow_WhenNotFound() {
        // Arrange
        var command = new DeleteBatchImageCommand(1L);
        when(batchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Batch not found"));
        verify(batchRepository).findById(1L);
        verifyNoMoreInteractions(batchRepository);
    }





}