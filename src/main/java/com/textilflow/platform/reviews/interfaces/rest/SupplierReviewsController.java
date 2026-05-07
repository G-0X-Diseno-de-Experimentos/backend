package com.textilflow.platform.reviews.interfaces.rest;

import com.textilflow.platform.reviews.domain.model.queries.CheckIfBusinessmanReviewedSupplierQuery;
import com.textilflow.platform.reviews.domain.model.queries.GetReviewByIdQuery;
import com.textilflow.platform.reviews.domain.model.queries.GetReviewsBySupplierIdQuery;
import com.textilflow.platform.reviews.domain.services.SupplierReviewCommandService;
import com.textilflow.platform.reviews.domain.services.SupplierReviewQueryService;
import com.textilflow.platform.reviews.interfaces.rest.resources.CreateSupplierReviewResource;
import com.textilflow.platform.reviews.interfaces.rest.resources.SupplierReviewResource;
import com.textilflow.platform.reviews.interfaces.rest.resources.UpdateSupplierReviewResource;
import com.textilflow.platform.reviews.interfaces.rest.transform.CreateSupplierReviewCommandFromResourceAssembler;
import com.textilflow.platform.reviews.interfaces.rest.transform.SupplierReviewResourceFromEntityAssembler;
import com.textilflow.platform.reviews.interfaces.rest.transform.UpdateSupplierReviewCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SupplierReviewsController
 * Controlador REST para manejar las reseñas de suppliers
 * Siguiendo DDD estricto - un solo controlador por contexto
 */
@RestController
@RequestMapping(value = "/api/v1/supplier-reviews", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Supplier Reviews", description = "Available Supplier Review Endpoints")
public class SupplierReviewsController {

    private final SupplierReviewCommandService supplierReviewCommandService;
    private final SupplierReviewQueryService supplierReviewQueryService;

    /**
     * Constructor
     * @param supplierReviewCommandService Service para comandos de reseñas
     * @param supplierReviewQueryService Service para queries de reseñas
     */
    public SupplierReviewsController(SupplierReviewCommandService supplierReviewCommandService,
                                     SupplierReviewQueryService supplierReviewQueryService) {
        this.supplierReviewCommandService = supplierReviewCommandService;
        this.supplierReviewQueryService = supplierReviewQueryService;
    }

    /**
     * POST /api/v1/supplier-reviews
     * Crear una nueva reseña de supplier
     */
    @PostMapping
    @Operation(summary = "Create a new supplier review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier review created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data or businessman already reviewed this supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier review not found after creation")
    })
    public ResponseEntity<SupplierReviewResource> createSupplierReview(@RequestBody CreateSupplierReviewResource resource) {
        var createSupplierReviewCommand = CreateSupplierReviewCommandFromResourceAssembler.toCommandFromResource(resource);
        var supplierReview = supplierReviewCommandService.handle(createSupplierReviewCommand);

        if (supplierReview.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var createdSupplierReview = supplierReview.get();
        var supplierReviewResource = SupplierReviewResourceFromEntityAssembler.toResourceFromEntity(createdSupplierReview);
        return new ResponseEntity<>(supplierReviewResource, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/supplier-reviews/supplier/{supplierId}
     * Obtener todas las reseñas de un supplier
     */
    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "Get all reviews for a specific supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No reviews found for the supplier")
    })
    public ResponseEntity<List<SupplierReviewResource>> getReviewsBySupplier(@PathVariable Long supplierId) {
        var getReviewsBySupplierIdQuery = new GetReviewsBySupplierIdQuery(supplierId);
        var reviews = supplierReviewQueryService.handle(getReviewsBySupplierIdQuery);

        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var reviewResources = reviews.stream()
                .map(SupplierReviewResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(reviewResources);
    }

    /**
     * GET /api/v1/supplier-reviews/check/{supplierId}/{businessmanId}
     * Verificar si un businessman ya reseñó a un supplier
     */
    @GetMapping("/check/{supplierId}/{businessmanId}")
    @Operation(summary = "Check if a businessman has already reviewed a supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier ID or businessman ID")
    })
    public ResponseEntity<Boolean> checkIfBusinessmanReviewedSupplier(@PathVariable Long supplierId,
                                                                      @PathVariable Long businessmanId) {
        var checkIfBusinessmanReviewedSupplierQuery = new CheckIfBusinessmanReviewedSupplierQuery(supplierId, businessmanId);
        var hasReviewed = supplierReviewQueryService.handle(checkIfBusinessmanReviewedSupplierQuery);
        return ResponseEntity.ok(hasReviewed);
    }

    /**
     * PUT /api/v1/supplier-reviews/{reviewId}
     * Actualizar una reseña existente
     */
    @PutMapping("/{reviewId}")
    @Operation(summary = "Update an existing supplier review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier review updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "404", description = "Supplier review not found")
    })
    public ResponseEntity<SupplierReviewResource> updateSupplierReview(@PathVariable Long reviewId,
                                                                       @RequestBody UpdateSupplierReviewResource resource) {
        var updateSupplierReviewCommand = UpdateSupplierReviewCommandFromResourceAssembler.toCommandFromResource(reviewId, resource);
        var updatedSupplierReview = supplierReviewCommandService.handle(updateSupplierReviewCommand);

        if (updatedSupplierReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var updatedSupplierReviewEntity = updatedSupplierReview.get();
        var updatedSupplierReviewResource = SupplierReviewResourceFromEntityAssembler.toResourceFromEntity(updatedSupplierReviewEntity);
        return ResponseEntity.ok(updatedSupplierReviewResource);
    }
}