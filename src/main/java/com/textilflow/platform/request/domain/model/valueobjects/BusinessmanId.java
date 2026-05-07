package com.textilflow.platform.request.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record BusinessmanId(Long value) {
    public BusinessmanId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Businessman ID must be positive");
        }
    }
}
