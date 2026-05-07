package com.textilflow.platform.observation.domain.services;

import com.textilflow.platform.observation.domain.model.aggregates.Observation;
import com.textilflow.platform.observation.domain.model.commands.CreateObservationCommand;
import com.textilflow.platform.observation.domain.model.commands.UpdateObservationCommand;
import com.textilflow.platform.observation.domain.model.commands.DeleteObservationCommand;
import com.textilflow.platform.observation.domain.model.commands.UploadObservationImageCommand;
import com.textilflow.platform.observation.domain.model.commands.DeleteObservationImageCommand;

import java.util.Optional;

/**
 * Observation command service interface
 */
public interface ObservationCommandService {

    /**
     * Handle create observation command
     */
    Optional<Observation> handle(CreateObservationCommand command);

    /**
     * Handle update observation command
     */
    Optional<Observation> handle(UpdateObservationCommand command);

    /**
     * Handle delete observation command
     */
    void handle(DeleteObservationCommand command);

    /**
     * Handle upload observation image command
     */
    Optional<Observation> handle(UploadObservationImageCommand command);

    /**
     * Handle delete observation image command
     */
    Optional<Observation> handle(DeleteObservationImageCommand command);
}