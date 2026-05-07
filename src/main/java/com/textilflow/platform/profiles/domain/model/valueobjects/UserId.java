package com.textilflow.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * User ID value object
 */
@Embeddable
public record UserId(Long value) {
    public UserId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
    }

    public UserId() {
        this(null);
    }
}