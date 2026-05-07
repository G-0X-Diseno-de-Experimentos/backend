package com.textilflow.platform.observation.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

/**
 * Command to upload observation image
 */
public record UploadObservationImageCommand(
        Long observationId,
        MultipartFile file
) {}
