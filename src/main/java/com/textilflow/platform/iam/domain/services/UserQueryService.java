package com.textilflow.platform.iam.domain.services;

import com.textilflow.platform.iam.domain.model.aggregates.User;
import com.textilflow.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.textilflow.platform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.Optional;

/**
 * User query service interface
 */
public interface UserQueryService {

    /**
     * Handle get user by ID query
     * @param query the get user by ID query
     * @return the user if found
     */
    Optional<User> handle(GetUserByIdQuery query);

    /**
     * Handle get user by email query
     * @param query the get user by email query
     * @return the user if found
     */
    Optional<User> handle(GetUserByEmailQuery query);
}