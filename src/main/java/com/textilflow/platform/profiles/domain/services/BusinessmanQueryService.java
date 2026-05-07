package com.textilflow.platform.profiles.domain.services;

import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import com.textilflow.platform.profiles.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Businessman query service interface
 */
public interface BusinessmanQueryService {

    /**
     * Handle get businessman by user ID query
     */
    Optional<Businessman> handle(GetBusinessmanByUserIdQuery query);

    /**
     * Handle get all businessmen query
     */
    List<Businessman> handle(GetAllBusinessmenQuery query);
}