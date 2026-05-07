package com.textilflow.platform.iam.interfaces.rest;

import com.textilflow.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.textilflow.platform.iam.domain.services.UserCommandService;
import com.textilflow.platform.iam.domain.services.UserQueryService;
import com.textilflow.platform.iam.interfaces.rest.resources.UpdateUserRoleResource;
import com.textilflow.platform.iam.interfaces.rest.resources.UserResource;
import com.textilflow.platform.iam.interfaces.rest.transform.UpdateUserRoleCommandFromResourceAssembler;
import com.textilflow.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Users controller for user management operations
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = "application/json")
@Tag(name = "Users", description = "User Management Endpoints")
public class UsersController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UsersController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    /**
     * Get user by ID
     * @param userId the user ID
     * @return the user resource
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Update user role
     * @param userId the user ID
     * @param updateUserRoleResource the update user role resource
     * @return the updated user resource
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<UserResource> updateUserRole(@PathVariable Long userId,
                                                       @RequestBody UpdateUserRoleResource updateUserRoleResource) {
        var updateUserRoleCommand = UpdateUserRoleCommandFromResourceAssembler
                .toCommandFromResource(userId, updateUserRoleResource);
        var updatedUser = userCommandService.handle(updateUserRoleCommand);
        if (updatedUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(updatedUser.get());
        return ResponseEntity.ok(userResource);
    }
}