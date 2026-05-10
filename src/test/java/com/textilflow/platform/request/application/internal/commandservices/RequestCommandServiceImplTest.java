package com.textilflow.platform.request.application.internal.commandservices;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import com.textilflow.platform.request.domain.model.commands.*;
import com.textilflow.platform.request.domain.model.valueobjects.*;
import com.textilflow.platform.request.infrastructure.persistence.jpa.repositories.RequestRepository;
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
class RequestCommandServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateBusinessSupplierRequestCommand) debe guardar y retornar el ID exitosamente (AAA)")
    void handle_CreateRequest_ShouldSaveAndReturnId() {
        // Arrange
        var command = mock(CreateBusinessSupplierRequestCommand.class);
        when(command.businessmanId()).thenReturn(10L);
        when(command.supplierId()).thenReturn(20L);
        when(command.batchType()).thenReturn("COTTON");
        when(command.color()).thenReturn("BLUE");
        when(command.quantity()).thenReturn(100);
        when(command.address()).thenReturn("Av. Principal 123");
        when(command.message()).thenReturn("Requiero tela de alta calidad");

        BusinessSupplierRequest savedRequestMock = mock(BusinessSupplierRequest.class);
        when(savedRequestMock.getId()).thenReturn(1L);
        when(requestRepository.save(any(BusinessSupplierRequest.class))).thenReturn(savedRequestMock);

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertEquals(1L, resultId);
        verify(requestRepository).save(any(BusinessSupplierRequest.class));
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    @DisplayName("handle(UpdateRequestStatusCommand) debe actualizar el estado si existe (AAA)")
    void handle_UpdateStatus_ShouldUpdate_WhenExists() {
        // Arrange
        var command = mock(UpdateRequestStatusCommand.class);
        when(command.requestId()).thenReturn(1L);
        when(command.status()).thenReturn(RequestStatus.ACCEPTED);
        when(command.message()).thenReturn("Aceptado por el proveedor");

        BusinessSupplierRequest request = spy(new BusinessSupplierRequest());
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.save(request)).thenReturn(request);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        verify(requestRepository).findById(1L);
        verify(request).updateStatus(RequestStatus.ACCEPTED, "Aceptado por el proveedor");
        verify(requestRepository).save(request);
    }

    @Test
    @DisplayName("handle(UpdateRequestStatusCommand) debe retornar Optional vacío si no existe (AAA)")
    void handle_UpdateStatus_ShouldReturnEmpty_WhenNotFound() {
        // Arrange
        var command = mock(UpdateRequestStatusCommand.class);
        when(command.requestId()).thenReturn(1L);
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isEmpty());
        verify(requestRepository).findById(1L);
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    @DisplayName("handle(UpdateRequestDetailsCommand) debe actualizar detalles si la solicitud existe (AAA)")
    void handle_UpdateDetails_ShouldUpdate_WhenExists() {
        // Arrange
        var command = mock(UpdateRequestDetailsCommand.class);
        when(command.requestId()).thenReturn(1L);
        when(command.batchType()).thenReturn("SILK");
        when(command.color()).thenReturn("RED");
        when(command.quantity()).thenReturn(200);
        when(command.address()).thenReturn("Av. Central 456");
        when(command.message()).thenReturn("Detalles actualizados");

        BusinessSupplierRequest request = spy(new BusinessSupplierRequest());
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.save(request)).thenReturn(request);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        verify(requestRepository).findById(1L);
        verify(request).updateRequestDetails(eq("Detalles actualizados"), any(BatchType.class), any(Color.class), any(Quantity.class), any(Address.class));
        verify(requestRepository).save(request);
    }

    @Test
    @DisplayName("handle(DeleteRequestCommand) debe eliminar por ID directamente (AAA)")
    void handle_DeleteRequest_ShouldCallRepositoryDelete() {
        // Arrange
        var command = mock(DeleteRequestCommand.class);
        when(command.requestId()).thenReturn(1L);

        // Act
        service.handle(command);

        // Assert
        verify(requestRepository).deleteById(1L);
        verifyNoMoreInteractions(requestRepository);
    }
}