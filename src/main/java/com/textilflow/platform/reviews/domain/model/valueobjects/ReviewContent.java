package com.textilflow.platform.reviews.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * ReviewContent Value Object
 * Representa el contenido/comentario de una reseña
 */
@Embeddable
public record ReviewContent(String content) {

    public ReviewContent {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Review content cannot be null or blank");
        }
        if (content.length() > 1000) {
            throw new IllegalArgumentException("Review content cannot exceed 1000 characters");
        }
    }

    public ReviewContent() {
        this(null);
    }
}