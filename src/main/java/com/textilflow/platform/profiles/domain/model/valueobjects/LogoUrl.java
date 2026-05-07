package com.textilflow.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Logo URL value object
 */
@Embeddable
public record LogoUrl(String url) {
    public LogoUrl {
        if (url != null && !url.isBlank() && !isValidUrl(url)) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }

    public LogoUrl() {
        this(null);
    }

    private static boolean isValidUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public boolean isEmpty() {
        return url == null || url.isBlank();
    }
}