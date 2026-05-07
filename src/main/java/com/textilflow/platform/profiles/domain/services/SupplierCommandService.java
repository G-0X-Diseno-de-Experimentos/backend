package com.textilflow.platform.profiles.domain.services;

import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import com.textilflow.platform.profiles.domain.model.commands.*;

import java.util.Optional;

/**
 * Supplier command service interface
 */
public interface SupplierCommandService {

    /**
     * Handle create supplier command
     */
    Optional<Supplier> handle(CreateSupplierCommand command);

    /**
     * Handle update supplier command
     */
    Optional<Supplier> handle(UpdateSupplierCommand command);

    /**
     * Handle upload logo command
     */
    Optional<Supplier> handle(UploadLogoCommand command);

    /**
     * Handle delete logo command
     */
    Optional<Supplier> handle(DeleteLogoCommand command);
}