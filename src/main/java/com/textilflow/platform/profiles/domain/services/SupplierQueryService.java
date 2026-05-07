package com.textilflow.platform.profiles.domain.services;

import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import com.textilflow.platform.profiles.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Supplier query service interface
 */
public interface SupplierQueryService {

    /**
     * Handle get supplier by user ID query
     */
    Optional<Supplier> handle(GetSupplierByUserIdQuery query);

    /**
     * Handle get all suppliers query
     */
    List<Supplier> handle(GetAllSuppliersQuery query);
}