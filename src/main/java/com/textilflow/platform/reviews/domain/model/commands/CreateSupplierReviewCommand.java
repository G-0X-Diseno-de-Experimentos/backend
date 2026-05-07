package com.textilflow.platform.reviews.domain.model.commands;

/**
 * Command to create a supplier review
 * @param supplierId the supplier ID (cannot be null or <= 0)
 * @param businessmanId the businessman ID (cannot be null or <= 0)
 * @param rating the rating value (1-5 stars)
 * @param reviewContent the review comment/content
 */
public record CreateSupplierReviewCommand(
        Long supplierId,
        Long businessmanId,
        Integer rating,
        String reviewContent
) {

    /**
     * Constructor with validation
     */
    public CreateSupplierReviewCommand {
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
    }
}