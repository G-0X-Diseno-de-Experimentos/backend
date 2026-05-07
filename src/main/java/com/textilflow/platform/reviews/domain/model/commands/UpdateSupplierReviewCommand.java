package com.textilflow.platform.reviews.domain.model.commands;

/**
 * Command to update an existing supplier review
 * @param reviewId the review ID (cannot be null or <= 0)
 * @param rating the new rating value (1-5 stars)
 * @param reviewContent the new review comment/content
 */
public record UpdateSupplierReviewCommand(
        Long reviewId,
        Integer rating,
        String reviewContent
) {

    /**
     * Constructor with validation
     */
    public UpdateSupplierReviewCommand {
        if (reviewId == null || reviewId <= 0) {
            throw new IllegalArgumentException("Review ID cannot be null or less than or equal to 0");
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (reviewContent == null || reviewContent.isBlank()) {
            throw new IllegalArgumentException("Review content cannot be null or blank");
        }
    }
}