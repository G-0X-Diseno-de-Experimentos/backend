package com.textilflow.platform.profiles.application.internal.commandservices;

import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import com.textilflow.platform.profiles.domain.model.commands.*;
import com.textilflow.platform.profiles.domain.model.events.BusinessmanCreatedEvent;
import com.textilflow.platform.profiles.domain.model.events.LogoUploadedEvent;
import com.textilflow.platform.profiles.domain.model.valueobjects.LogoUrl;
import com.textilflow.platform.profiles.domain.services.ProfileImageService;
import com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessmanRepository;
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
class BusinessmanCommandServiceImplTest {

    @Mock private BusinessmanRepository businessmanRepository;
    @Mock private ProfileImageService profileImageService;
    @Mock private ExternalIamService externalIamService;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks private BusinessmanCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateBusinessmanCommand) debe crear el perfil exitosamente (AAA)")
    void handle_CreateBusinessman_ShouldCreateSuccessfully() {
        // Arrange
        var command = mock(CreateBusinessmanCommand.class);
        when(command.userId()).thenReturn(100L);
        when(command.companyName()).thenReturn("Textil Corp");
        when(command.ruc()).thenReturn("20123456789");
        when(command.businessType()).thenReturn("MANUFACTURING");
        when(command.description()).thenReturn("Desc");
        when(command.website()).thenReturn("http://web.com");

        when(externalIamService.userExists(100L)).thenReturn(true);
        when(businessmanRepository.existsByUserId(100L)).thenReturn(false);

        Businessman savedMock = spy(new Businessman(100L, "Textil Corp", "20123456789", "MANUFACTURING", "Desc", "http://web.com"));
        when(businessmanRepository.save(any(Businessman.class))).thenReturn(savedMock);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(savedMock, result.get());
        verify(externalIamService).userExists(100L);
        verify(businessmanRepository).existsByUserId(100L);
        verify(businessmanRepository).save(any(Businessman.class));
        verify(eventPublisher).publishEvent(any(BusinessmanCreatedEvent.class));
    }

    @Test
    @DisplayName("handle(CreateBusinessmanCommand) debe lanzar excepción si el usuario no existe en IAM (AAA)")
    void handle_CreateBusinessman_ShouldThrow_WhenUserNotFoundInIam() {
        // Arrange
        var command = mock(CreateBusinessmanCommand.class);
        when(command.userId()).thenReturn(100L);
        when(externalIamService.userExists(100L)).thenReturn(false);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("User not found in IAM context"));
        verify(externalIamService).userExists(100L);
        verifyNoInteractions(businessmanRepository, eventPublisher);
    }

    @Test
    @DisplayName("handle(CreateBusinessmanCommand) debe lanzar excepción si el perfil ya existe (AAA)")
    void handle_CreateBusinessman_ShouldThrow_WhenProfileAlreadyExists() {
        // Arrange
        var command = mock(CreateBusinessmanCommand.class);
        when(command.userId()).thenReturn(100L);
        when(externalIamService.userExists(100L)).thenReturn(true);
        when(businessmanRepository.existsByUserId(100L)).thenReturn(true);

        // Act + Assert
        var ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertTrue(ex.getMessage().contains("Businessman profile already exists"));
        verify(externalIamService).userExists(100L);
        verify(businessmanRepository).existsByUserId(100L);
        verifyNoMoreInteractions(businessmanRepository);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateBusinessmanCommand) debe actualizar información y propagar a IAM (AAA)")
    void handle_UpdateBusinessman_ShouldUpdateSuccessfully() {
        // Arrange
        var command = mock(UpdateBusinessmanCommand.class);
        when(command.userId()).thenReturn(100L);
        when(command.companyName()).thenReturn("New Corp");
        when(command.ruc()).thenReturn("20987654321");
        when(command.businessType()).thenReturn("RETAIL");
        when(command.description()).thenReturn("New Desc");
        when(command.website()).thenReturn("http://newweb.com");

        when(command.name()).thenReturn("John");
        when(command.email()).thenReturn("john@mail.com");
        when(command.country()).thenReturn("Peru");
        when(command.city()).thenReturn("Lima");
        when(command.address()).thenReturn("Av. Pardo");
        when(command.phone()).thenReturn("123");

        Businessman businessman = spy(new Businessman(100L));
        when(businessmanRepository.findByUserId(100L)).thenReturn(Optional.of(businessman));
        when(businessmanRepository.save(businessman)).thenReturn(businessman);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("New Corp", businessman.getCompanyNameValue());
        verify(businessmanRepository).findByUserId(100L);
        verify(externalIamService).updateUserData(100L, "John", "john@mail.com", "Peru", "Lima", "Av. Pardo", "123");
        verify(businessmanRepository).save(businessman);
    }

    @Test
    @DisplayName("handle(UpdateBusinessmanCommand) debe lanzar excepción si no se encuentra el perfil (AAA)")
    void handle_UpdateBusinessman_ShouldThrow_WhenNotFound() {
        // Arrange
        var command = mock(UpdateBusinessmanCommand.class);
        when(command.userId()).thenReturn(100L);
        when(businessmanRepository.findByUserId(100L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> service.handle(command));
        verify(businessmanRepository).findByUserId(100L);
        verifyNoMoreInteractions(businessmanRepository);
        verifyNoInteractions(externalIamService);
    }

    @Test
    @DisplayName("handle(UploadLogoCommand) debe eliminar el logo anterior, subir uno nuevo y guardar (AAA)")
    void handle_UploadLogo_ShouldReplaceAndSave() {
        // Arrange
        MultipartFile fileMock = mock(MultipartFile.class);
        var command = mock(UploadLogoCommand.class);
        when(command.userId()).thenReturn(100L);
        when(command.logoFile()).thenReturn(fileMock);

        Businessman businessman = spy(new Businessman(100L));
        businessman.updateLogo(new LogoUrl("http://old.url/logo.png"));

        when(businessmanRepository.findByUserId(100L)).thenReturn(Optional.of(businessman));
        when(profileImageService.uploadImage(100L, fileMock, "BUSINESSMAN")).thenReturn("http://new.url/logo.png");
        when(businessmanRepository.save(businessman)).thenReturn(businessman);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("http://new.url/logo.png", businessman.getLogoUrlValue());
        verify(profileImageService).deleteImage("http://old.url/logo.png");
        verify(profileImageService).uploadImage(100L, fileMock, "BUSINESSMAN");
        verify(businessmanRepository).save(businessman);
        verify(eventPublisher).publishEvent(any(LogoUploadedEvent.class));
    }

    @Test
    @DisplayName("handle(DeleteLogoCommand) debe eliminar imagen de nube y vaciar el campo (AAA)")
    void handle_DeleteLogo_ShouldClearLogoField() {
        // Arrange
        var command = mock(DeleteLogoCommand.class);
        when(command.userId()).thenReturn(100L);

        Businessman businessman = spy(new Businessman(100L));
        businessman.updateLogo(new LogoUrl("http://cloud.url/logo.png"));

        when(businessmanRepository.findByUserId(100L)).thenReturn(Optional.of(businessman));
        when(businessmanRepository.save(businessman)).thenReturn(businessman);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertNull(businessman.getLogoUrlValue());
        verify(profileImageService).deleteImage("http://cloud.url/logo.png");
        verify(businessmanRepository).save(businessman);
    }
}