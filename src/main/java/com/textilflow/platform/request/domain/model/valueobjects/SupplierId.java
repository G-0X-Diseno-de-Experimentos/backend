package com.textilflow.platform.request.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record SupplierId(Long value) {
    public SupplierId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Supplier ID must be positive");
        }
    }
}
