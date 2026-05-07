package com.textilflow.platform.reviews.interfaces.rest.resources;

/**
 * CreateSupplierReviewResource
 * Resource (DTO) para crear una nueva reseña de supplier
 * Basado en el frontend: addReview(supplierId, businessmanId, rating, comment, businessmanName?)
 */
public record CreateSupplierReviewResource(
        Long supplierId,
        Long businessmanId,
        Integer rating,
        String reviewContent
) {

    /**
     * Constructor con validaciones
     * Valida que todos los campos requeridos estén presentes y sean válidos
     */
    public CreateSupplierReviewResource {
        if (supplierId == null || supplierId <= 0) {
            throw new IllegalArgumentException("Supplier ID cannot be null or less than or equal to 0");
        }
        if (businessmanId == null || businessmanId <= 0) {
            throw new IllegalArgumentException("Businessman ID cannot be null or less than or equal to 0");
        }
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