package com.textilflow.platform.reviews.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * BusinessmanId Value Object
 * Representa el identificador único de un empresario en el contexto de reviews
 */
@Embeddable
public record BusinessmanId(Long businessmanId) {

    public BusinessmanId {
        if (businessmanId == null) {
            throw new IllegalArgumentException("Businessman ID cannot be null");
        }
        if (businessmanId <= 0) {
            throw new IllegalArgumentException("Businessman ID must be positive");
        }
    }

    public BusinessmanId(String businessmanId) {
        this(Long.valueOf(businessmanId));
    }

    public String value() {
        return businessmanId.toString();
    }
}