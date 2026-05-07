package com.textilflow.platform.profiles.interfaces.rest;

import com.textilflow.platform.profiles.domain.model.queries.GetAllSuppliersQuery;
import com.textilflow.platform.profiles.domain.model.queries.GetSupplierByUserIdQuery;
import com.textilflow.platform.profiles.domain.services.SupplierCommandService;
import com.textilflow.platform.profiles.domain.services.SupplierQueryService;
import com.textilflow.platform.profiles.interfaces.rest.resources.SupplierResource;
import com.textilflow.platform.profiles.interfaces.rest.resources.CreateSupplierResource;
import com.textilflow.platform.profiles.interfaces.rest.resources.UpdateSupplierResource;
import com.textilflow.platform.profiles.interfaces.rest.transform.SupplierResourceFromEntityAssembler;
import com.textilflow.platform.profiles.interfaces.rest.transform.CreateSupplierCommandFromResourceAssembler;
import com.textilflow.platform.profiles.interfaces.rest.transform.UpdateSupplierCommandFromResourceAssembler;
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
 * Suppliers controller for supplier management operations
 */
@RestController
@RequestMapping(value = "/api/v1/suppliers", produces = "application/json")
@Tag(name = "Suppliers", description = "Supplier Management Endpoints")
public class SuppliersController {

    private final SupplierCommandService supplierCommandService;
    private final SupplierQueryService supplierQueryService;
    private final ExternalIamService externalIamService;

    public SuppliersController(SupplierCommandService supplierCommandService,
                               SupplierQueryService supplierQueryService,
                               ExternalIamService externalIamService) {
        this.supplierCommandService = supplierCommandService;
        this.supplierQueryService = supplierQueryService;
        this.externalIamService = externalIamService;
    }

    /**
     * Create supplier profile
     */
    @PostMapping("/{userId}")
    @Operation(summary = "Create supplier profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier profile created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<SupplierResource> createSupplier(@PathVariable Long userId,
                                                           @RequestBody CreateSupplierResource resource) {
        var command = CreateSupplierCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var supplier = supplierCommandService.handle(command);

        if (supplier.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var supplierResource = SupplierResourceFromEntityAssembler
                .toResourceFromEntity(supplier.get(), externalIamService);
        return new ResponseEntity<>(supplierResource, HttpStatus.CREATED);
    }

    /**
     * Update supplier profile
     */
    @PutMapping("/{userId}")
    @Operation(summary = "Update supplier profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier profile updated"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<SupplierResource> updateSupplier(@PathVariable Long userId,
                                                           @RequestBody UpdateSupplierResource resource) {
        var command = UpdateSupplierCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var supplier = supplierCommandService.handle(command);

        if (supplier.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var supplierResource = SupplierResourceFromEntityAssembler
                .toResourceFromEntity(supplier.get(), externalIamService);
        return ResponseEntity.ok(supplierResource);
    }

    /**
     * Get supplier by user ID
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get supplier by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<SupplierResource> getSupplierByUserId(@PathVariable Long userId) {
        var query = new GetSupplierByUserIdQuery(userId);
        var supplier = supplierQueryService.handle(query);

        if (supplier.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var supplierResource = SupplierResourceFromEntityAssembler
                .toResourceFromEntity(supplier.get(), externalIamService);
        return ResponseEntity.ok(supplierResource);
    }

    /**
     * Get all suppliers
     */
    @GetMapping
    @Operation(summary = "Get all suppliers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers found")
    })
    public ResponseEntity<List<SupplierResource>> getAllSuppliers() {
        var query = new GetAllSuppliersQuery();
        var suppliers = supplierQueryService.handle(query);

        var supplierResources = suppliers.stream()
                .map(supplier -> SupplierResourceFromEntityAssembler
                        .toResourceFromEntity(supplier, externalIamService))
                .toList();

        return ResponseEntity.ok(supplierResources);
    }
}
