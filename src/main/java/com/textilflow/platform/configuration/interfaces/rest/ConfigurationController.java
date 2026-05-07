package com.textilflow.platform.configuration.interfaces.rest;

import com.textilflow.platform.configuration.domain.model.queries.GetConfigurationByUserIdQuery;
import com.textilflow.platform.configuration.domain.services.ConfigurationCommandService;
import com.textilflow.platform.configuration.domain.services.ConfigurationQueryService;
import com.textilflow.platform.configuration.interfaces.rest.resources.ConfigurationResource;
import com.textilflow.platform.configuration.interfaces.rest.resources.CreateConfigurationResource;
import com.textilflow.platform.configuration.interfaces.rest.resources.UpdateConfigurationResource;
import com.textilflow.platform.configuration.interfaces.rest.transform.ConfigurationResourceFromEntityAssembler;
import com.textilflow.platform.configuration.interfaces.rest.transform.CreateConfigurationCommandFromResourceAssembler;
import com.textilflow.platform.configuration.interfaces.rest.transform.UpdateConfigurationCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Configuration REST controller
 * Handles HTTP requests for configuration management
 */
@RestController
@RequestMapping(value = "/api/v1/configurations", produces = "application/json")
@Tag(name = "Configurations", description = "Configuration Management Endpoints")
public class ConfigurationController {

    private final ConfigurationCommandService configurationCommandService;
    private final ConfigurationQueryService configurationQueryService;

    public ConfigurationController(ConfigurationCommandService configurationCommandService,
                                   ConfigurationQueryService configurationQueryService) {
        this.configurationCommandService = configurationCommandService;
        this.configurationQueryService = configurationQueryService;
    }

    /**
     * Get configuration by user ID
     * GET /api/v1/configurations?userId={userId}
     */
    @GetMapping
    @Operation(summary = "Get configuration by user ID",
            description = "Retrieve user configuration settings by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration found"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    public ResponseEntity<ConfigurationResource> getConfigurationByUserId(@RequestParam Long userId) {
        var query = new GetConfigurationByUserIdQuery(userId);
        var configuration = configurationQueryService.handle(query);

        if (configuration.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resource = ConfigurationResourceFromEntityAssembler.toResourceFromEntity(configuration.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Create new configuration
     * POST /api/v1/configurations
     */
    @PostMapping
    @Operation(summary = "Create new configuration",
            description = "Create a new user configuration with default or specified settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Configuration created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Configuration already exists for user")
    })
    public ResponseEntity<ConfigurationResource> createConfiguration(@RequestBody CreateConfigurationResource resource) {
        System.out.println("=== CONTROLLER DEBUG: Recibiendo petición createConfiguration ===");
        System.out.println("Resource recibido: " + resource);

        try {
            System.out.println("=== Convirtiendo resource a command ===");
            var command = CreateConfigurationCommandFromResourceAssembler.toCommandFromResource(resource);
            System.out.println("Command creado exitosamente");

            System.out.println("=== Ejecutando command service ===");
            var configuration = configurationCommandService.handle(command);

            if (configuration.isEmpty()) {
                System.out.println("ERROR: ConfigurationCommandService retornó Optional vacío");
                return ResponseEntity.badRequest().build();
            }

            System.out.println("=== Convirtiendo entity a resource ===");
            var configurationResource = ConfigurationResourceFromEntityAssembler.toResourceFromEntity(configuration.get());
            System.out.println("SUCCESS: Configuración creada exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(configurationResource);

        } catch (IllegalArgumentException e) {
            System.out.println("ERROR en createConfiguration: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERROR INESPERADO en createConfiguration: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update existing configuration
     * PUT /api/v1/configurations/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update configuration",
            description = "Update an existing user configuration settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration updated successfully"),
            @ApiResponse(responseCode = "404", description = "Configuration not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ConfigurationResource> updateConfiguration(@PathVariable Long id,
                                                                     @RequestBody UpdateConfigurationResource resource) {
        try {
            var command = UpdateConfigurationCommandFromResourceAssembler.toCommandFromResource(id, resource);
            var configuration = configurationCommandService.handle(command);

            if (configuration.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var configurationResource = ConfigurationResourceFromEntityAssembler.toResourceFromEntity(configuration.get());
            return ResponseEntity.ok(configurationResource);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}