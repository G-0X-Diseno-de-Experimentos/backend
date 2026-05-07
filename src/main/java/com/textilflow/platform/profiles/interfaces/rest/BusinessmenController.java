package com.textilflow.platform.profiles.interfaces.rest;

import com.textilflow.platform.profiles.domain.model.queries.GetAllBusinessmenQuery;
import com.textilflow.platform.profiles.domain.model.queries.GetBusinessmanByUserIdQuery;
import com.textilflow.platform.profiles.domain.services.BusinessmanCommandService;
import com.textilflow.platform.profiles.domain.services.BusinessmanQueryService;
import com.textilflow.platform.profiles.interfaces.rest.resources.BusinessmanResource;
import com.textilflow.platform.profiles.interfaces.rest.resources.CreateBusinessmanResource;
import com.textilflow.platform.profiles.interfaces.rest.resources.UpdateBusinessmanResource;
import com.textilflow.platform.profiles.interfaces.rest.transform.BusinessmanResourceFromEntityAssembler;
import com.textilflow.platform.profiles.interfaces.rest.transform.CreateBusinessmanCommandFromResourceAssembler;
import com.textilflow.platform.profiles.interfaces.rest.transform.UpdateBusinessmanCommandFromResourceAssembler;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Businessmen controller for businessman management operations
 */
@RestController
@RequestMapping(value = "/api/v1/businessmen", produces = "application/json")
@Tag(name = "Businessmen", description = "Businessman Management Endpoints")
public class BusinessmenController {

    private final BusinessmanCommandService businessmanCommandService;
    private final BusinessmanQueryService businessmanQueryService;
    private final ExternalIamService externalIamService;

    public BusinessmenController(BusinessmanCommandService businessmanCommandService,
                                 BusinessmanQueryService businessmanQueryService,
                                 ExternalIamService externalIamService) {
        this.businessmanCommandService = businessmanCommandService;
        this.businessmanQueryService = businessmanQueryService;
        this.externalIamService = externalIamService;
    }

    /**
     * Create businessman profile
     */
    @PostMapping("/{userId}")
    @Operation(summary = "Create businessman profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Businessman profile created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<BusinessmanResource> createBusinessman(@PathVariable Long userId,
                                                                 @RequestBody CreateBusinessmanResource resource) {
        var command = CreateBusinessmanCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var businessman = businessmanCommandService.handle(command);

        if (businessman.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var businessmanResource = BusinessmanResourceFromEntityAssembler
                .toResourceFromEntity(businessman.get(), externalIamService);
        return new ResponseEntity<>(businessmanResource, HttpStatus.CREATED);
    }

    /**
     * Update businessman profile
     */
    @PutMapping("/{userId}")
    @Operation(summary = "Update businessman profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Businessman profile updated"),
            @ApiResponse(responseCode = "404", description = "Businessman not found")
    })
    public ResponseEntity<BusinessmanResource> updateBusinessman(@PathVariable Long userId,
                                                                 @RequestBody UpdateBusinessmanResource resource) {
        var command = UpdateBusinessmanCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var businessman = businessmanCommandService.handle(command);

        if (businessman.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var businessmanResource = BusinessmanResourceFromEntityAssembler
                .toResourceFromEntity(businessman.get(), externalIamService);
        return ResponseEntity.ok(businessmanResource);
    }

    /**
     * Get businessman by user ID
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get businessman by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Businessman found"),
            @ApiResponse(responseCode = "404", description = "Businessman not found")
    })
    public ResponseEntity<BusinessmanResource> getBusinessmanByUserId(@PathVariable Long userId) {
        var query = new GetBusinessmanByUserIdQuery(userId);
        var businessman = businessmanQueryService.handle(query);

        if (businessman.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var businessmanResource = BusinessmanResourceFromEntityAssembler
                .toResourceFromEntity(businessman.get(), externalIamService);
        return ResponseEntity.ok(businessmanResource);
    }

    /**
     * Get all businessmen
     */
    @GetMapping
    @Operation(summary = "Get all businessmen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Businessmen found")
    })
    public ResponseEntity<List<BusinessmanResource>> getAllBusinessmen() {
        var query = new GetAllBusinessmenQuery();
        var businessmen = businessmanQueryService.handle(query);

        var businessmenResources = businessmen.stream()
                .map(businessman -> BusinessmanResourceFromEntityAssembler
                        .toResourceFromEntity(businessman, externalIamService))
                .toList();

        return ResponseEntity.ok(businessmenResources);
    }
}