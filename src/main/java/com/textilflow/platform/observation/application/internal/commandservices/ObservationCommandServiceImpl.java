package com.textilflow.platform.observation.application.internal.commandservices;

import com.textilflow.platform.observation.domain.model.aggregates.Observation;
import com.textilflow.platform.observation.domain.model.commands.CreateObservationCommand;
import com.textilflow.platform.observation.domain.model.commands.UpdateObservationCommand;
import com.textilflow.platform.observation.domain.model.commands.DeleteObservationCommand;
import com.textilflow.platform.observation.domain.model.commands.UploadObservationImageCommand;
import com.textilflow.platform.observation.domain.model.commands.DeleteObservationImageCommand;
import com.textilflow.platform.observation.domain.model.valueobjects.BatchCode;
import com.textilflow.platform.observation.domain.model.valueobjects.ImageUrl;
import com.textilflow.platform.observation.domain.model.valueobjects.ObservationStatus;
import com.textilflow.platform.observation.domain.services.ObservationCommandService;
import com.textilflow.platform.observation.domain.services.ObservationImageService;
import com.textilflow.platform.observation.infrastructure.persistence.jpa.repositories.ObservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ObservationCommandServiceImpl implements ObservationCommandService {

    private final ObservationRepository observationRepository;
    private final ObservationImageService observationImageService;

    public ObservationCommandServiceImpl(ObservationRepository observationRepository,
                                         ObservationImageService observationImageService) {
        this.observationRepository = observationRepository;
        this.observationImageService = observationImageService;
    }

    @Override
    @Transactional
    public Optional<Observation> handle(CreateObservationCommand command) {
        var batchCode = new BatchCode(command.batchCode());
        var imageUrl = command.imageUrl() != null ? new ImageUrl(command.imageUrl()) : null;
        var status = ObservationStatus.valueOf(command.status().toUpperCase());

        var observation = new Observation(
                command.batchId(),
                batchCode,
                command.businessmanId(),
                command.supplierId(),
                command.reason(),
                imageUrl,
                status
        );

        return Optional.of(observationRepository.save(observation));
    }

    @Override
    @Transactional
    public Optional<Observation> handle(UpdateObservationCommand command) {
        var observation = observationRepository.findById(command.observationId());
        if (observation.isPresent()) {
            observation.get().updateInformation(command);
            return Optional.of(observationRepository.save(observation.get()));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void handle(DeleteObservationCommand command) {
        var observation = observationRepository.findById(command.observationId());
        if (observation.isPresent()) {
            // Delete image from cloud storage if exists
            String imageUrl = observation.get().getImageUrlValue();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                observationImageService.deleteImage(imageUrl);
            }
            observationRepository.deleteById(command.observationId());
        }
    }

    @Override
    @Transactional
    public Optional<Observation> handle(UploadObservationImageCommand command) {
        var observation = observationRepository.findById(command.observationId());
        if (observation.isPresent()) {
            try {
                // Delete existing image if present
                String existingImageUrl = observation.get().getImageUrlValue();
                if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
                    observationImageService.deleteImage(existingImageUrl);
                }

                // Upload new image
                String imageUrl = observationImageService.uploadImage(command.observationId(), command.file());
                observation.get().updateImage(imageUrl);

                return Optional.of(observationRepository.save(observation.get()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload observation image: " + e.getMessage(), e);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Observation> handle(DeleteObservationImageCommand command) {
        var observation = observationRepository.findById(command.observationId());
        if (observation.isPresent()) {
            String imageUrl = observation.get().getImageUrlValue();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                observationImageService.deleteImage(imageUrl);
                observation.get().deleteImage();
                return Optional.of(observationRepository.save(observation.get()));
            }
        }
        return Optional.empty();
    }
}