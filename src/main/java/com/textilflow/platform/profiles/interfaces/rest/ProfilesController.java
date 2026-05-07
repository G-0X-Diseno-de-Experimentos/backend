package com.textilflow.platform.profiles.interfaces.rest;

import com.textilflow.platform.profiles.domain.model.queries.GetBusinessmanByUserIdQuery;
import com.textilflow.platform.profiles.domain.model.queries.GetSupplierByUserIdQuery;
import com.textilflow.platform.profiles.domain.services.BusinessmanQueryService;
import com.textilflow.platform.profiles.domain.services.SupplierQueryService;
import com.textilflow.platform.profiles.interfaces.rest.resources.ProfileResource;
import com.textilflow.platform.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Profiles controller for complete profile management
 */
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = "application/json")
@Tag(name = "Profiles", description = "Complete Profile Management Endpoints")
public class ProfilesController {

    private final BusinessmanQueryService businessmanQueryService;
    private final SupplierQueryService supplierQueryService;
    private final ExternalIamService externalIamService;

    public ProfilesController(BusinessmanQueryService businessmanQueryService,
                              SupplierQueryService supplierQueryService,
                              ExternalIamService externalIamService) {
        this.businessmanQueryService = businessmanQueryService;
        this.supplierQueryService = supplierQueryService;
        this.externalIamService = externalIamService;
    }

    /**
     * Get complete profile by user ID
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get complete profile by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ProfileResource> getProfileByUserId(@PathVariable Long userId) {
        // Verify user exists
        if (!externalIamService.userExists(userId)) {
            return ResponseEntity.notFound().build();
        }

        // Get user role
        String userRole = externalIamService.getUserRole(userId);

        // Get businessman or supplier profile based on role
        var businessman = businessmanQueryService.handle(new GetBusinessmanByUserIdQuery(userId)).orElse(null);
        var supplier = supplierQueryService.handle(new GetSupplierByUserIdQuery(userId)).orElse(null);

        var profileResource = ProfileResourceFromEntityAssembler
                .toResourceFromEntities(userRole, externalIamService, userId, businessman, supplier);

        return ResponseEntity.ok(profileResource);
    }
}
