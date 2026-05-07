package com.textilflow.platform.profiles.interfaces.rest;

import com.textilflow.platform.profiles.domain.model.commands.UploadLogoCommand;
import com.textilflow.platform.profiles.domain.model.commands.DeleteLogoCommand;
import com.textilflow.platform.profiles.domain.services.BusinessmanCommandService;
import com.textilflow.platform.profiles.domain.services.SupplierCommandService;
import com.textilflow.platform.profiles.interfaces.rest.resources.LogoUploadResource;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Profile images controller for logo upload/delete operations
 */
@RestController
@RequestMapping(value = "/api/v1/profiles/{userId}/images", produces = "application/json")
@Tag(name = "Profile Images", description = "Profile Image Management Endpoints")
public class ProfileImagesController {

    private final BusinessmanCommandService businessmanCommandService;
    private final SupplierCommandService supplierCommandService;
    private final ExternalIamService externalIamService;

    public ProfileImagesController(BusinessmanCommandService businessmanCommandService,
                                   SupplierCommandService supplierCommandService,
                                   ExternalIamService externalIamService) {
        this.businessmanCommandService = businessmanCommandService;
        this.supplierCommandService = supplierCommandService;
        this.externalIamService = externalIamService;
    }

    /**
     * Upload profile logo
     */
    @PostMapping(value = "/logo", consumes = "multipart/form-data")
    @Operation(
            summary = "Upload profile logo",
            description = "Upload a logo image for the user profile. Accepts JPG, PNG files."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logo uploaded successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "400", description = "Invalid file")
    })
    public ResponseEntity<LogoUploadResource> uploadLogo(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        // Get user role to determine which service to use
        String userRole = externalIamService.getUserRole(userId);

        var uploadCommand = new UploadLogoCommand(userId, file);
        String logoUrl;

        switch (userRole) {
            case "BUSINESSMAN" -> {
                var businessman = businessmanCommandService.handle(uploadCommand);
                if (businessman.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                logoUrl = businessman.get().getLogoUrlValue();
            }
            case "SUPPLIER" -> {
                var supplier = supplierCommandService.handle(uploadCommand);
                if (supplier.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                logoUrl = supplier.get().getLogoUrlValue();
            }
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }

        var response = new LogoUploadResource(userId, logoUrl, "Logo uploaded successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Delete profile logo
     */
    @DeleteMapping("/logo")
    @Operation(summary = "Delete profile logo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logo deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<LogoUploadResource> deleteLogo(@PathVariable Long userId) {
        // Get user role to determine which service to use
        String userRole = externalIamService.getUserRole(userId);

        var deleteCommand = new DeleteLogoCommand(userId);

        switch (userRole) {
            case "BUSINESSMAN" -> {
                var businessman = businessmanCommandService.handle(deleteCommand);
                if (businessman.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
            }
            case "SUPPLIER" -> {
                var supplier = supplierCommandService.handle(deleteCommand);
                if (supplier.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
            }
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }

        var response = new LogoUploadResource(userId, null, "Logo deleted successfully");
        return ResponseEntity.ok(response);
    }
}