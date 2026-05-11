package com.textilflow.platform.observation.application.internal.commandservices;

import com.textilflow.platform.observation.domain.model.aggregates.Observation;
import com.textilflow.platform.observation.domain.model.commands.*;
import com.textilflow.platform.observation.domain.model.valueobjects.BatchCode;
import com.textilflow.platform.observation.domain.model.valueobjects.ImageUrl;
import com.textilflow.platform.observation.domain.model.valueobjects.ObservationStatus;
import com.textilflow.platform.observation.domain.services.ObservationImageService;
import com.textilflow.platform.observation.infrastructure.persistence.jpa.repositories.ObservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObservationCommandServiceImplTest {

    @Mock private ObservationRepository observationRepository;
    @Mock private ObservationImageService observationImageService;

    @InjectMocks private ObservationCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateObservationCommand) debe crear y guardar una observación exitosamente (AAA)")
    void handle_CreateObservation_ShouldSaveSuccessfully() {
        // Arrange
        // Se asume la estructura canónica inferida del servicio
        var command = new CreateObservationCommand(10L, "B001", 100L, 200L, "Falla en costura", "http://cloud.url/img.jpg", "PENDIENTE");
        Observation savedObservationMock = mock(Observation.class);
        when(observationRepository.save(any(Observation.class))).thenReturn(savedObservationMock);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(savedObservationMock, result.get());
        verify(observationRepository).save(any(Observation.class));
        verifyNoMoreInteractions(observationRepository);
        verifyNoInteractions(observationImageService);
    }

    @Test
    @DisplayName("handle(UpdateObservationCommand) debe retornar Optional vacío si no existe (AAA)")
    void handle_UpdateObservation_ShouldReturnEmpty_WhenNotFound() {
        // Arrange
        var command = new UpdateObservationCommand(1L, "Nueva razón", "http://cloud.url/new.jpg", "EN_REVISION");
        when(observationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isEmpty());
        verify(observationRepository).findById(1L);
        verifyNoMoreInteractions(observationRepository);
    }

    @Test
    @DisplayName("handle(UpdateObservationCommand) debe actualizar y guardar si existe (AAA)")
    void handle_UpdateObservation_ShouldUpdate_WhenFound() {
        // Arrange
        var command = new UpdateObservationCommand(1L, "Nueva razón", "http://cloud.url/new.jpg", "EN_REVISION");
        Observation observation = spy(new Observation());
        when(observationRepository.findById(1L)).thenReturn(Optional.of(observation));
        when(observationRepository.save(observation)).thenReturn(observation);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        verify(observationRepository).findById(1L);
        verify(observation).updateInformation(command);
        verify(observationRepository).save(observation);
    }

    @Test
    @DisplayName("handle(DeleteObservationCommand) debe eliminar imagen en nube y entidad si existe (AAA)")
    void handle_DeleteObservation_ShouldDeleteImageAndEntity() {
        // Arrange
        var command = new DeleteObservationCommand(1L);
        Observation observation = mock(Observation.class);
        when(observation.getImageUrlValue()).thenReturn("http://cloud.url/img.jpg");
        when(observationRepository.findById(1L)).thenReturn(Optional.of(observation));

        // Act
        service.handle(command);

        // Assert
        verify(observationRepository).findById(1L);
        verify(observationImageService).deleteImage("http://cloud.url/img.jpg");
        verify(observationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("handle(DeleteObservationCommand) solo debe eliminar entidad si no hay imagen (AAA)")
    void handle_DeleteObservation_ShouldOnlyDeleteEntity_WhenNoImage() {
        // Arrange
        var command = new DeleteObservationCommand(1L);
        Observation observation = mock(Observation.class);
        when(observation.getImageUrlValue()).thenReturn(null);
        when(observationRepository.findById(1L)).thenReturn(Optional.of(observation));

        // Act
        service.handle(command);

        // Assert
        verify(observationRepository).findById(1L);
        verify(observationRepository).deleteById(1L);
        verifyNoInteractions(observationImageService);
    }

    @Test
    @DisplayName("handle(UploadObservationImageCommand) debe reemplazar imagen y guardar (AAA)")
    void handle_UploadObservationImage_ShouldReplaceAndSave() throws Exception {
        // Arrange
        MultipartFile fileMock = mock(MultipartFile.class);
        var command = new UploadObservationImageCommand(1L, fileMock);
        Observation observation = spy( new Observation(
                1L,
                new BatchCode("B001"),
                100L,
                200L,
                "Razón",
                new ImageUrl("http://old.url/img.jpg"),
                ObservationStatus.PENDIENTE
        ));

        when(observationRepository.findById(1L)).thenReturn(Optional.of(observation));
        when(observationImageService.uploadImage(1L, fileMock)).thenReturn("http://new.url/img.jpg");
        when(observationRepository.save(observation)).thenReturn(observation);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("http://new.url/img.jpg", result.get().getImageUrlValue());
        verify(observationImageService).deleteImage("http://old.url/img.jpg");
        verify(observationImageService).uploadImage(1L, fileMock);
        verify(observationRepository).save(observation);
    }

    @Test
    @DisplayName("handle(DeleteObservationImageCommand) debe eliminar imagen de nube y limpiar campo (AAA)")
    void handle_DeleteObservationImage_ShouldClearField() {
        // Arrange
        var command = new DeleteObservationImageCommand(1L);
        Observation observation = spy(new Observation(
                1L,
                new BatchCode("B001"),
                100L,
                200L,
                "Razón",
                new ImageUrl("http://cloud.url/img.jpg"),
                ObservationStatus.PENDIENTE
        ));
        when(observationRepository.findById(1L)).thenReturn(Optional.of(observation));
        when(observationRepository.save(observation)).thenReturn(observation);

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertNull(result.get().getImageUrlValue());
        verify(observationImageService).deleteImage("http://cloud.url/img.jpg");
        verify(observation).deleteImage();
        verify(observationRepository).save(observation);
    }

    @Test
    @DisplayName("handle(CreateObservationCommand) should throw when status is invalid (AAA)")
    void handle_CreateObservation_ShouldThrow_WhenStatusInvalid() {
        // Arrange
        var command = new CreateObservationCommand(
                10L, "B001", 100L, 200L,
                "Falla", "url", "INVALID_STATUS"
        );

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.handle(command));

        verifyNoInteractions(observationRepository, observationImageService);
    }

    @Test
    @DisplayName("handle(DeleteObservationCommand) should do nothing when not found (AAA)")
    void handle_DeleteObservation_ShouldDoNothing_WhenNotFound() {
        // Arrange
        var command = new DeleteObservationCommand(1L);
        when(observationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        service.handle(command);

        // Assert
        verify(observationRepository).findById(1L);
        verifyNoMoreInteractions(observationRepository);
        verifyNoInteractions(observationImageService);
    }

    @Test
    @DisplayName("handle(UploadObservationImageCommand) should throw when upload fails (AAA)")
    void handle_Upload_ShouldThrow_WhenImageServiceFails() {
        // Arrange
        var file = mock(MultipartFile.class);
        var command = new UploadObservationImageCommand(1L, file);

        Observation obs = mock(Observation.class);

        when(observationRepository.findById(1L)).thenReturn(Optional.of(obs));
        when(observationImageService.uploadImage(1L, file))
                .thenThrow(new RuntimeException("cloud error"));

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> service.handle(command));

        verify(observationImageService).uploadImage(1L, file);
    }

    @Test
    @DisplayName("handle(UploadObservationImageCommand) should return empty when observation not found (AAA)")
    void handle_Upload_ShouldReturnEmpty_WhenNotFound() {
        // Arrange
        var file = mock(MultipartFile.class);
        var command = new UploadObservationImageCommand(1L, file);

        when(observationRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isEmpty());
        verify(observationRepository).findById(1L);
        verifyNoInteractions(observationImageService);
    }

    @Test
    @DisplayName("handle(DeleteObservationImageCommand) should return empty when not found (AAA)")
    void handle_DeleteObservationImage_ShouldReturnEmpty_WhenNotFound() {
        // Arrange
        var command = new DeleteObservationImageCommand(1L);

        when(observationRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isEmpty());
        verify(observationRepository).findById(1L);
        verifyNoInteractions(observationImageService);
    }

    @Test
    @DisplayName("handle(CreateObservationCommand) should handle null imageUrl (AAA)")
    void handle_CreateObservation_ShouldHandleNullImageUrl() {
        // Arrange
        var command = new CreateObservationCommand(
                10L, "B001", 100L, 200L,
                "Falla", null, "PENDIENTE"
        );

        when(observationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = service.handle(command);

        // Assert
        assertTrue(result.isPresent());
        verify(observationRepository).save(any());
    }

    @Test
    @DisplayName("deleteImage debe establecer imageUrl en null (AAA)")
    void deleteImage_ShouldSetImageUrlToNull() {
        // Arrange
        Observation observation = new Observation(
                1L,
                new BatchCode("B001"),
                100L,
                200L,
                "Razón",
                new ImageUrl("http://cloud.url/img.jpg"),
                ObservationStatus.PENDIENTE
        );

        // Act
        observation.deleteImage();

        // Assert
        assertNull(observation.getImageUrlValue());
    }

    @Test
    @DisplayName("updateImage debe reemplazar imageUrl correctamente (AAA)")
    void updateImage_ShouldReplaceImageUrl() {
        // Arrange
        Observation observation = new Observation(
                1L,
                new BatchCode("B001"),
                100L,
                200L,
                "Razón",
                new ImageUrl("http://old.url/img.jpg"),
                ObservationStatus.PENDIENTE
        );

        // Act
        observation.updateImage("http://new.url/img.jpg");

        // Assert
        assertEquals("http://new.url/img.jpg", observation.getImageUrlValue());
    }

    @Test
    @DisplayName("getImageUrlValue debe retornar null cuando imageUrl es null (AAA)")
    void getImageUrlValue_ShouldReturnNull_WhenImageIsNull() {
        // Arrange
        Observation observation = new Observation(
                1L,
                new BatchCode("B001"),
                100L,
                200L,
                "Razón",
                null,
                ObservationStatus.PENDIENTE
        );

        // Act
        String result = observation.getImageUrlValue();

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("updateInformation debe normalizar status a uppercase (AAA)")
    void updateInformation_ShouldNormalizeStatusToUpperCase() {
        // Arrange
        Observation observation = new Observation(
                1L,
                new BatchCode("B001"),
                100L,
                200L,
                "Old",
                null,
                ObservationStatus.PENDIENTE
        );

        var command = new UpdateObservationCommand(
                1L,
                "New Reason",
                null,
                "en_revision"
        );

        // Act
        observation.updateInformation(command);

        // Assert
        assertEquals(ObservationStatus.EN_REVISION, observation.getStatus());
    }
}