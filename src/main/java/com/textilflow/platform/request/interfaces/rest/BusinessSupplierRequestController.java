package com.textilflow.platform.request.interfaces.rest;

import com.textilflow.platform.request.domain.model.commands.DeleteRequestCommand;
import com.textilflow.platform.request.domain.model.queries.*;
import com.textilflow.platform.request.domain.services.RequestCommandService;
import com.textilflow.platform.request.domain.services.RequestQueryService;
import com.textilflow.platform.request.interfaces.rest.resources.*;
import com.textilflow.platform.request.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/business-supplier-requests")
@Tag(name = "Business Supplier Requests", description = "Business Supplier Request Management Endpoints")
public class BusinessSupplierRequestController {

    private final RequestCommandService requestCommandService;
    private final RequestQueryService requestQueryService;

    public BusinessSupplierRequestController(RequestCommandService requestCommandService,
                                             RequestQueryService requestQueryService) {
        this.requestCommandService = requestCommandService;
        this.requestQueryService = requestQueryService;
    }

    @PostMapping
    @Operation(summary = "Create new request", description = "Creates a new business-supplier request.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Request created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<BusinessSupplierRequestResource> createRequest(@RequestBody CreateBusinessSupplierRequestResource resource) {
        var command = CreateBusinessSupplierRequestCommandFromResourceAssembler.toCommandFromResource(resource);
        var requestId = requestCommandService.handle(command);

        if (requestId == null) {
            return ResponseEntity.badRequest().build();
        }

        var query = new GetRequestByIdQuery(requestId);
        var request = requestQueryService.handle(query);

        if (request.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var requestResource = BusinessSupplierRequestResourceFromEntityAssembler.toResourceFromEntity(request.get());
        return new ResponseEntity<>(requestResource, HttpStatus.CREATED);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get request by ID", description = "Retrieves a business-supplier request by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request found"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    public ResponseEntity<BusinessSupplierRequestResource> getRequestById(@PathVariable Long requestId) {
        var query = new GetRequestByIdQuery(requestId);
        var request = requestQueryService.handle(query);

        if (request.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var requestResource = BusinessSupplierRequestResourceFromEntityAssembler.toResourceFromEntity(request.get());
        return ResponseEntity.ok(requestResource);
    }

    @GetMapping
    @Operation(summary = "Get all requests", description = "Retrieves all business-supplier requests.")
    @ApiResponse(responseCode = "200", description = "List of requests retrieved successfully")
    public ResponseEntity<List<BusinessSupplierRequestResource>> getAllRequests() {
        var query = new GetAllRequestsQuery();
        var requests = requestQueryService.handle(query);

        var requestResources = requests.stream()
                .map(BusinessSupplierRequestResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(requestResources);
    }

    @GetMapping("/businessman/{businessmanId}")
    @Operation(summary = "Get requests by businessman ID", description = "Retrieves requests made by a specific businessman.")
    @ApiResponse(responseCode = "200", description = "List of requests found for the businessman")
    public ResponseEntity<List<BusinessSupplierRequestResource>> getRequestsByBusinessmanId(@PathVariable Long businessmanId) {
        var query = new GetRequestsByBusinessmanIdQuery(businessmanId);
        var requests = requestQueryService.handle(query);

        var requestResources = requests.stream()
                .map(BusinessSupplierRequestResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(requestResources);
    }

    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "Get requests by supplier ID", description = "Retrieves requests made to a specific supplier.")
    @ApiResponse(responseCode = "200", description = "List of requests found for the supplier")
    public ResponseEntity<List<BusinessSupplierRequestResource>> getRequestsBySupplierId(@PathVariable Long supplierId) {
        var query = new GetRequestsBySupplierIdQuery(supplierId);
        var requests = requestQueryService.handle(query);

        var requestResources = requests.stream()
                .map(BusinessSupplierRequestResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(requestResources);
    }

    @PutMapping("/{requestId}/status")
    @Operation(summary = "Update request status", description = "Updates the status of an existing request.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    public ResponseEntity<BusinessSupplierRequestResource> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestBody UpdateRequestStatusResource resource) {

        var command = UpdateRequestStatusCommandFromResourceAssembler.toCommandFromResource(requestId, resource);
        var updatedRequest = requestCommandService.handle(command);

        if (updatedRequest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var requestResource = BusinessSupplierRequestResourceFromEntityAssembler.toResourceFromEntity(updatedRequest.get());
        return ResponseEntity.ok(requestResource);
    }

    @PutMapping("/{requestId}/details")
    @Operation(summary = "Update request details", description = "Updates the details (not status) of a request.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request details updated successfully"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    public ResponseEntity<BusinessSupplierRequestResource> updateRequestDetails(
            @PathVariable Long requestId,
            @RequestBody UpdateRequestDetailsResource resource) {

        var command = UpdateRequestDetailsCommandFromResourceAssembler.toCommandFromResource(requestId, resource);
        var updatedRequest = requestCommandService.handle(command);

        if (updatedRequest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var requestResource = BusinessSupplierRequestResourceFromEntityAssembler.toResourceFromEntity(updatedRequest.get());
        return ResponseEntity.ok(requestResource);
    }

    @DeleteMapping("/{requestId}")
    @Operation(summary = "Delete request", description = "Deletes a request by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Request deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    public ResponseEntity<Void> deleteRequest(@PathVariable Long requestId) {
        var command = new DeleteRequestCommand(requestId);
        requestCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
