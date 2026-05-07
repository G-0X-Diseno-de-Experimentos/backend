package com.textilflow.platform.iam.domain.services;

import com.textilflow.platform.iam.domain.model.aggregates.User;
import com.textilflow.platform.iam.domain.model.commands.*;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

/**
 * User command service interface
 */
public interface UserCommandService {

    /**
     * Handle sign up command
     * @param command the sign up command
     * @return the created user
     */
    Optional<User> handle(SignUpCommand command);

    /**
     * Handle sign in command
     * @param command the sign in command
     * @return the authenticated user and token
     */
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);

    /**
     * Handle update user role command
     * @param command the update user role command
     * @return the updated user
     */
    Optional<User> handle(UpdateUserRoleCommand command);

    /**
     * Handle update user data command  // ✅ AGREGAR ESTE MÉTODO
     * @param command the update user data command
     * @return the updated user
     */
    Optional<User> handle(UpdateUserDataCommand command);
    /**
     * Handle forgot password command
     */
    boolean handle(ForgotPasswordCommand command);

    /**
     * Handle reset password command
     */
    boolean handle(ResetPasswordCommand command);
}