package com.textilflow.platform.batches.interfaces.rest;

import com.textilflow.platform.batches.domain.model.commands.CreateBatchCommand;
import com.textilflow.platform.batches.domain.model.commands.DeleteBatchCommand;
import com.textilflow.platform.batches.domain.model.commands.UpdateBatchCommand;
import com.textilflow.platform.batches.domain.model.commands.UpdateBatchImageCommand;
import com.textilflow.platform.batches.domain.model.commands.DeleteBatchImageCommand;
import com.textilflow.platform.batches.domain.model.queries.GetAllBatchesQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchByIdQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchesBySupplierIdQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchesByBusinessmanIdQuery;
import com.textilflow.platform.batches.domain.services.BatchCommandService;
import com.textilflow.platform.batches.domain.services.BatchQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.textilflow.platform.batches.infraestructure.persistence.cloudinary.BatchCloudinaryService;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/batches", produces = "application/json")
@Tag(name = "Batches", description = "Batch Management Endpoints")
public class BatchController {
    private final BatchCommandService batchCommandService;
    private final BatchQueryService batchQueryService;
    private final BatchCloudinaryService cloudinaryService;

    public BatchController(BatchCommandService batchCommandService, BatchQueryService batchQueryService, BatchCloudinaryService cloudinaryService) {
        this.batchCommandService = batchCommandService;
        this.batchQueryService = batchQueryService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    @Operation(summary = "Create a new batch", description = "Creates a new production batch with specified details")
    public ResponseEntity<?> createBatch(@RequestBody CreateBatchCommand command) {
        var batchId = batchCommandService.handle(command);
        return ResponseEntity.ok(batchId);
    }

    @GetMapping
    @Operation(summary = "Get all batches", description = "Retrieves a list of all production batches")
    public ResponseEntity<?> getAllBatches() {
        var batches = batchQueryService.handle(new GetAllBatchesQuery());
        return ResponseEntity.ok(batches);
    }

    @GetMapping("/{batchId}")
    @Operation(summary = "Get batch by ID", description = "Retrieves a specific batch by its unique identifier")
    public ResponseEntity<?> getBatchById(@PathVariable Long batchId) {
        var batch = batchQueryService.handle(new GetBatchByIdQuery(batchId));
        return ResponseEntity.ok(batch);
    }

    @PutMapping("/{batchId}")
    @Operation(summary = "Update batch", description = "Updates an existing batch with new information")
    public ResponseEntity<?> updateBatch(@PathVariable Long batchId, @RequestBody UpdateBatchCommand command) {
        var updateCommand = new UpdateBatchCommand(batchId, command.code(), command.client(), command.businessmanId(), command.supplierId(), command.fabricType(), command.color(), command.quantity(), command.price(), command.observations(), command.address(), command.date(), command.status(), command.imageUrl());
        var updatedBatch = batchCommandService.handle(updateCommand);
        return ResponseEntity.ok(updatedBatch);
    }

    @DeleteMapping("/{batchId}")
    @Operation(summary = "Delete batch", description = "Removes a batch from the system permanently")
    public ResponseEntity<?> deleteBatch(@PathVariable Long batchId) {
        batchCommandService.handle(new DeleteBatchCommand(batchId));
        return ResponseEntity.ok("Batch deleted successfully");
    }

    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "Get batches by supplier ID", description = "Retrieves all batches for a specific supplier")
    public ResponseEntity<?> getBatchesBySupplierId(@PathVariable Long supplierId) {
        var batches = batchQueryService.handle(new GetBatchesBySupplierIdQuery(supplierId));
        return ResponseEntity.ok(batches);
    }

    @GetMapping("/businessman/{businessmanId}")
    @Operation(summary = "Get batches by businessman ID", description = "Retrieves all batches for a specific businessman")
    public ResponseEntity<?> getBatchesByBusinessmanId(@PathVariable Long businessmanId) {
        var batches = batchQueryService.handle(new GetBatchesByBusinessmanIdQuery(businessmanId));
        return ResponseEntity.ok(batches);
    }

    @PostMapping(value = "/{batchId}/image", consumes = "multipart/form-data")
    @Operation(summary = "Update batch image", description = "Uploads an image file to Cloudinary and updates the batch image URL")
    public ResponseEntity<?> updateBatchImage(@PathVariable Long batchId, @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("File must be an image");
            }

            // Upload to Cloudinary
            String imageUrl = cloudinaryService.uploadBatchImage(file);

            // Update batch with new image URL
            var updateCommand = new UpdateBatchImageCommand(batchId, imageUrl);
            var updatedBatch = batchCommandService.handle(updateCommand);

            return ResponseEntity.ok(updatedBatch);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{batchId}/image")
    @Operation(summary = "Delete batch image", description = "Deletes the batch image from Cloudinary and removes the URL from batch")
    public ResponseEntity<?> deleteBatchImage(@PathVariable Long batchId) {
        try {
            var batch = batchQueryService.handle(new GetBatchByIdQuery(batchId));
            if (batch.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            String currentImageUrl = batch.get().getImageUrl();
            cloudinaryService.deleteBatchImage(currentImageUrl);

            var deleteCommand = new DeleteBatchImageCommand(batchId);
            var updatedBatch = batchCommandService.handle(deleteCommand);

            return ResponseEntity.ok(updatedBatch);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/test/profiles/{userId}")
    @Operation(summary = "Test profiles ACL", description = "Test endpoint to verify profiles context facade")
    public ResponseEntity<?> testProfilesACL(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok("Testing userId: " + userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


}