package com.textilflow.platform.reviews.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Rating Value Object
 * Representa la calificación de una reseña (1-5 estrellas)
 */
@Embeddable
public record Rating(Integer value) {

    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;

    public Rating {
        if (value == null) {
            throw new IllegalArgumentException("Rating cannot be null");
        }
        if (value < MIN_RATING || value > MAX_RATING) {
            throw new IllegalArgumentException(
                    String.format("Rating must be between %d and %d", MIN_RATING, MAX_RATING)
            );
        }
    }

    public Rating(String value) {
        this(Integer.valueOf(value));
    }

    public boolean isExcellent() {
        return value >= 4;
    }

    public boolean isPoor() {
        return value <= 2;
    }

    public String getDescription() {
        return switch (value) {
            case 1 -> "Muy malo";
            case 2 -> "Malo";
            case 3 -> "Regular";
            case 4 -> "Bueno";
            case 5 -> "Excelente";
            default -> "Desconocido";
        };
    }
}