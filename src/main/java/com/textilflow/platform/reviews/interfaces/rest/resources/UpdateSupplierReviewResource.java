package com.textilflow.platform.reviews.interfaces.rest.resources;

/**
 * UpdateSupplierReviewResource
 * Resource (DTO) para actualizar una reseña existente de supplier
 * Basado en el frontend: updateReview(reviewId, rating, comment)
 */
public record UpdateSupplierReviewResource(
        Integer rating,
        String reviewContent
) {

    /**
     * Constructor con validaciones
     * Valida que todos los campos requeridos estén presentes y sean válidos
     */
    public UpdateSupplierReviewResource {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (reviewContent == null || reviewContent.isBlank()) {
            throw new IllegalArgumentException("Review content cannot be null or blank");
        }
        if (reviewContent.length() > 1000) {
            throw new IllegalArgumentException("Review content cannot exceed 1000 characters");
        }
    }
}