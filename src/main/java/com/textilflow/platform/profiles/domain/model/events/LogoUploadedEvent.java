package com.textilflow.platform.profiles.domain.model.events;

/**
 * Event fired when a logo is uploaded
 */
public record LogoUploadedEvent(
        Long userId,
        String logoUrl,
        String userType // "BUSINESSMAN" or "SUPPLIER"
) {
}