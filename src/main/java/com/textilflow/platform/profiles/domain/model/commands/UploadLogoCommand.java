package com.textilflow.platform.profiles.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

/**
 * Upload logo command
 */
public record UploadLogoCommand(
        Long userId,
        MultipartFile logoFile
) {
}