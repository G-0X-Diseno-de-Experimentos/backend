package com.textilflow.platform.observation.interfaces.rest;

import com.textilflow.platform.observation.domain.model.commands.UploadObservationImageCommand;
import com.textilflow.platform.observation.domain.model.commands.DeleteObservationImageCommand;
import com.textilflow.platform.observation.domain.services.ObservationCommandService;
import com.textilflow.platform.observation.interfaces.resources.ObservationImageUploadResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Observation images controller for image upload/delete operations
 */
@RestController
@RequestMapping(value = "/api/v1/observations/{observationId}/images", produces = "application/json")
@Tag(name = "Observation Images", description = "Observation Image Management Endpoints")
public class ObservationImagesController {

    private final ObservationCommandService observationCommandService;

    public ObservationImagesController(ObservationCommandService observationCommandService) {
        this.observationCommandService = observationCommandService;
    }

    /**
     * Upload observation image
     */
    @PostMapping(consumes = "multipart/form-data")
    @Operation(
            summary = "Upload observation image",
            description = "Upload an image for the observation. Accepts JPG, PNG files."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "404", description = "Observation not found"),
            @ApiResponse(responseCode = "400", description = "Invalid file")
    })
    public ResponseEntity<ObservationImageUploadResource> uploadImage(
            @PathVariable Long observationId,
            @RequestParam("file") MultipartFile file) {

        var uploadCommand = new UploadObservationImageCommand(observationId, file);
        var observation = observationCommandService.handle(uploadCommand);

        if (observation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String imageUrl = observation.get().getImageUrlValue();
        var response = new ObservationImageUploadResource(observationId, imageUrl, "Image uploaded successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Delete observation image
     */
    @DeleteMapping
    @Operation(summary = "Delete observation image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Observation not found")
    })
    public ResponseEntity<ObservationImageUploadResource> deleteImage(@PathVariable Long observationId) {
        var deleteCommand = new DeleteObservationImageCommand(observationId);
        var observation = observationCommandService.handle(deleteCommand);

        if (observation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var response = new ObservationImageUploadResource(observationId, null, "Image deleted successfully");
        return ResponseEntity.ok(response);
    }
}
