package com.textilflow.platform.profiles.application.internal.commandservices;

import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import com.textilflow.platform.profiles.domain.model.commands.*;
import com.textilflow.platform.profiles.domain.model.events.LogoUploadedEvent;
import com.textilflow.platform.profiles.domain.model.events.SupplierCreatedEvent;
import com.textilflow.platform.profiles.domain.model.valueobjects.LogoUrl;
import com.textilflow.platform.profiles.domain.services.ProfileImageService;
import com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories.SupplierRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierCommandServiceImplTest {

    @Mock private SupplierRepository supplierRepository;
    @Mock private ProfileImageService profileImageService;
    @Mock private ExternalIamService externalIamService;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks private SupplierCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateSupplierCommand) debe crear el perfil exitosamente (AAA)")
    void handle_CreateSupplier_ShouldCreateSuccessfully() {
        // Arrange
        var command = mock(CreateSupplierCommand.class);
        when(command.userId()).thenReturn(200L);
        when(command.companyName()).thenReturn("Fabrics SA");
        when(command.ruc()).thenReturn("20555555555");
        when(command.specialization()).thenReturn("COTTON");
        when(command.description()).thenReturn("Desc");
        when(command.certifications()).thenReturn("ISO 9001");

        when(externalIamService.userExists(200L)).thenReturn(true);
        when(supplierRepository.existsByUserId(200L)).thenReturn(false);

        Supplier savedMock = spy(new Supplier(200L, "Fabrics SA", "20555555555", "COTTON", "Desc", "ISO 9001"));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(savedMock);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(savedMock, result.get());
        verify(externalIamService).userExists(200L);
        verify(supplierRepository).existsByUserId(200L);
        verify(supplierRepository).save(any(Supplier.class));
        verify(eventPublisher).publishEvent(any(SupplierCreatedEvent.class));
    }

    @Test
    @DisplayName("handle(CreateSupplierCommand) debe lanzar excepción si el perfil ya existe (AAA)")
    void handle_CreateSupplier_ShouldThrow_WhenProfileAlreadyExists() {
        // Arrange
        var command = mock(CreateSupplierCommand.class);
        when(command.userId()).thenReturn(200L);
        when(externalIamService.userExists(200L)).thenReturn(true);
        when(supplierRepository.existsByUserId(200L)).thenReturn(true);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Supplier profile already exists"));
        verify(externalIamService).userExists(200L);
        verify(supplierRepository).existsByUserId(200L);
        verifyNoMoreInteractions(supplierRepository);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateSupplierCommand) debe actualizar información y propagar a IAM (AAA)")
    void handle_UpdateSupplier_ShouldUpdateSuccessfully() {
        // Arrange
        var command = mock(UpdateSupplierCommand.class);
        when(command.userId()).thenReturn(200L);
        when(command.companyName()).thenReturn("Updated Fabrics");
        when(command.ruc()).thenReturn("20666666666");
        when(command.specialization()).thenReturn("SYNTHETIC");
        when(command.description()).thenReturn("New Desc");
        when(command.certifications()).thenReturn("ISO 14001");

        when(command.name()).thenReturn("Alice");
        when(command.email()).thenReturn("alice@mail.com");
        when(command.country()).thenReturn("Peru");
        when(command.city()).thenReturn("Arequipa");
        when(command.address()).thenReturn("Calle 1");
        when(command.phone()).thenReturn("999");

        Supplier supplier = spy(new Supplier(200L));
        when(supplierRepository.findByUserId(200L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Updated Fabrics", supplier.getCompanyNameValue());
        verify(supplierRepository).findByUserId(200L);
        verify(externalIamService).updateUserData(200L, "Alice", "alice@mail.com", "Peru", "Arequipa", "Calle 1", "999");
        verify(supplierRepository).save(supplier);
    }

    @Test
    @DisplayName("handle(UploadLogoCommand) en Supplier debe subir logo y notificar evento (AAA)")
    void handle_UploadLogo_ShouldProcessSuccessfully() {
        // Arrange
        MultipartFile fileMock = mock(MultipartFile.class);
        var command = mock(UploadLogoCommand.class);
        when(command.userId()).thenReturn(200L);
        when(command.logoFile()).thenReturn(fileMock);

        Supplier supplier = spy(new Supplier(200L));
        when(supplierRepository.findByUserId(200L)).thenReturn(Optional.of(supplier));
        when(profileImageService.uploadImage(200L, fileMock, "SUPPLIER")).thenReturn("http://new.url/supplier.png");
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("http://new.url/supplier.png", supplier.getLogoUrlValue());
        verify(profileImageService).uploadImage(200L, fileMock, "SUPPLIER");
        verify(supplierRepository).save(supplier);
        verify(eventPublisher).publishEvent(any(LogoUploadedEvent.class));
    }

    @Test
    @DisplayName("handle(DeleteLogoCommand) en Supplier debe limpiar el campo correctamente (AAA)")
    void handle_DeleteLogo_ShouldClearLogo() {
        // Arrange
        var command = mock(DeleteLogoCommand.class);
        when(command.userId()).thenReturn(200L);

        Supplier supplier = spy(new Supplier(200L));
        supplier.updateLogo(new LogoUrl("http://cloud/logo.png"));

        when(supplierRepository.findByUserId(200L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertNull(supplier.getLogoUrlValue());
        verify(profileImageService).deleteImage("http://cloud/logo.png");
        verify(supplierRepository).save(supplier);
    }

    @Test
    @DisplayName("handle(CreateSupplierCommand) debe fallar si IAM no encuentra usuario")
    void handle_CreateSupplier_ShouldThrow_WhenIamUserNotFound() {

        // Arrange
        var command = mock(CreateSupplierCommand.class);
        when(command.userId()).thenReturn(200L);

        when(externalIamService.userExists(200L)).thenReturn(false);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> service.handle(command));

        verifyNoInteractions(supplierRepository);
    }

    @Test
    @DisplayName("handle(UploadLogoCommand) no debe borrar logo si no existe previo")
    void handle_UploadLogo_ShouldNotDelete_WhenNoLogo() {

        // Arrange
        MultipartFile fileMock = mock(MultipartFile.class);
        var command = mock(UploadLogoCommand.class);

        when(command.userId()).thenReturn(200L);
        when(command.logoFile()).thenReturn(fileMock);

        Supplier supplier = spy(new Supplier(200L));

        when(supplierRepository.findByUserId(200L))
                .thenReturn(Optional.of(supplier));

        when(profileImageService.uploadImage(200L, fileMock, "SUPPLIER"))
                .thenReturn("http://cloud/logo.png");

        when(supplierRepository.save(supplier)).thenReturn(supplier);

        // Act
        service.handle(command);

        // Assert
        verify(profileImageService, never()).deleteImage(anyString());
    }

    @Test
    @DisplayName("handle(UploadLogoCommand) debe fallar si uploadImage lanza error")
    void handle_UploadLogo_ShouldThrow_WhenUploadFails() {

        // Arrange
        MultipartFile fileMock = mock(MultipartFile.class);
        var command = mock(UploadLogoCommand.class);

        when(command.userId()).thenReturn(200L);
        when(command.logoFile()).thenReturn(fileMock);

        Supplier supplier = spy(new Supplier(200L));

        when(supplierRepository.findByUserId(200L))
                .thenReturn(Optional.of(supplier));

        when(profileImageService.uploadImage(anyLong(), any(), anyString()))
                .thenThrow(new RuntimeException("Upload failed"));

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> service.handle(command));
    }
}