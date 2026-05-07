package com.textilflow.platform.request.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record BatchType(String value) {
    public BatchType {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Batch type cannot be null or blank");
        }
    }
}