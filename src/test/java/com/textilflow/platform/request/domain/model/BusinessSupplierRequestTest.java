package com.textilflow.platform.request.domain.model;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import com.textilflow.platform.request.domain.model.valueobjects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BusinessSupplierRequestTest {

    @Test
    @DisplayName("Constructor debe inicializar correctamente el aggregate en estado PENDING")
    void constructor_ShouldInitializeCorrectly() {

        // Arrange
        var businessmanId = new BusinessmanId(1L);
        var supplierId = new SupplierId(2L);
        var batchType = new BatchType("COTTON");
        var color = new Color("BLUE");
        var quantity = new Quantity(100);
        var address = new Address("Lima");

        // Act
        var request = new BusinessSupplierRequest(
                businessmanId,
                supplierId,
                "Mensaje inicial",
                batchType,
                color,
                quantity,
                address
        );

        // Assert
        assertEquals(RequestStatus.PENDING, request.getStatus());
        assertTrue(request.isPending());
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

    @Test
    @DisplayName("updateRequestDetails debe permitir valores null sin romper invariantes")
    void shouldHandleNullValuesInUpdateRequestDetails() {

        // Arrange
        var request = new BusinessSupplierRequest();

        // Act
        request.updateRequestDetails(
                "msg",
                null,
                null,
                null,
                null
        );

        // Assert
        assertEquals("msg", request.getMessage());
        assertNull(request.getBatchType());
        assertNull(request.getColor());
        assertNull(request.getQuantity());
        assertNull(request.getAddress());
    }

    @Test
    @DisplayName("nuevo request siempre debe iniciar en PENDING (invariante de dominio)")
    void shouldAlwaysStartAsPending() {

        // Arrange
        var request = new BusinessSupplierRequest(
                new BusinessmanId(1L),
                new SupplierId(2L),
                "msg",
                new BatchType("COTTON"),
                new Color("BLUE"),
                new Quantity(100),
                new Address("Lima")
        );

        // Assert
        assertEquals(RequestStatus.PENDING, request.getStatus());
        assertTrue(request.isPending());
        assertFalse(request.isAccepted());
        assertFalse(request.isRejected());
        assertFalse(request.isCancelled());
    }

}