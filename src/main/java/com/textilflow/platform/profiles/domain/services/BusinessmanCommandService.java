package com.textilflow.platform.profiles.domain.services;

import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import com.textilflow.platform.profiles.domain.model.commands.*;

import java.util.Optional;

/**
 * Businessman command service interface
 */
public interface BusinessmanCommandService {

    /**
     * Handle create businessman command
     */
    Optional<Businessman> handle(CreateBusinessmanCommand command);

    /**
     * Handle update businessman command
     */
    Optional<Businessman> handle(UpdateBusinessmanCommand command);

    /**
     * Handle upload logo command
     */
    Optional<Businessman> handle(UploadLogoCommand command);

    /**
     * Handle delete logo command
     */
    Optional<Businessman> handle(DeleteLogoCommand command);
}
