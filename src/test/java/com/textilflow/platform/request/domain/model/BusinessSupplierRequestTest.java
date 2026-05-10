package com.textilflow.platform.request.domain.model;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import com.textilflow.platform.request.domain.model.valueobjects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BusinessSupplierRequestTest {

    @Test
    @DisplayName("Constructor completo debe inicializar campos correctamente en estado PENDING (AAA)")
    void fullConstructor_ShouldInitializeCorrectly() {
        // Arrange
        BusinessmanId businessmanId = mock(BusinessmanId.class);
        SupplierId supplierId = mock(SupplierId.class);
        BatchType batchType = mock(BatchType.class);
        Color color = mock(Color.class);
        Quantity quantity = mock(Quantity.class);
        Address address = mock(Address.class);

        // Act
        var request = new BusinessSupplierRequest(businessmanId, supplierId, "Mensaje inicial", batchType, color, quantity, address);

        // Assert
        assertSame(businessmanId, request.getBusinessmanId());
        assertSame(supplierId, request.getSupplierId());
        assertEquals("Mensaje inicial", request.getMessage());
        assertSame(batchType, request.getBatchType());
        assertSame(color, request.getColor());
        assertSame(quantity, request.getQuantity());
        assertSame(address, request.getAddress());

        // Verificamos las banderas lógicas de estado sin depender de la colección interna de eventos
        assertTrue(request.isPending());
        assertFalse(request.isAccepted());
        assertFalse(request.isRejected());
        assertFalse(request.isCancelled());
        assertEquals(RequestStatus.PENDING, request.getStatus());
    }

    @Test
    @DisplayName("updateStatus debe cambiar el estado y el mensaje asignado correctamente (AAA)")
    void updateStatus_ShouldModifyFields() {
        // Arrange
        var request = new BusinessSupplierRequest();

        // Act
        request.updateStatus(RequestStatus.ACCEPTED, "Solicitud aceptada");

        // Assert
        assertEquals(RequestStatus.ACCEPTED, request.getStatus());
        assertEquals("Solicitud aceptada", request.getMessage());
        assertTrue(request.isAccepted());
        assertFalse(request.isPending());
    }

    @Test
    @DisplayName("updateRequestDetails debe actualizar todos los detalles y el mensaje especificados (AAA)")
    void updateRequestDetails_ShouldModifyDetails() {
        // Arrange
        var request = new BusinessSupplierRequest();
        BatchType batchType = mock(BatchType.class);
        Color color = mock(Color.class);
        Quantity quantity = mock(Quantity.class);
        Address address = mock(Address.class);

        // Act
        request.updateRequestDetails("Detalles actualizados", batchType, color, quantity, address);

        // Assert
        assertEquals("Detalles actualizados", request.getMessage());
        assertSame(batchType, request.getBatchType());
        assertSame(color, request.getColor());
        assertSame(quantity, request.getQuantity());
        assertSame(address, request.getAddress());
    }

    @Test
    @DisplayName("Banderas isRejected e isCancelled deben responder de acuerdo al estado actual asignado (AAA)")
    void flags_ShouldRespondToStatus() {
        // Arrange
        var request = new BusinessSupplierRequest();

        // Act & Assert - Probando REJECTED
        request.updateStatus(RequestStatus.REJECTED, "Rechazado por el proveedor");
        assertTrue(request.isRejected());
        assertFalse(request.isCancelled());

        // Act & Assert - Probando CANCELLED
        request.updateStatus(RequestStatus.CANCELLED, "Cancelado por el empresario");
        assertTrue(request.isCancelled());
        assertFalse(request.isRejected());
    }
}