package com.textilflow.platform.reviews.domain.model.queries;

/**
 * Query to get a review by its ID
 * @param reviewId the review ID to find
 */
public record GetReviewByIdQuery(Long reviewId) {

    /**
     * Constructor with validation
     */
    public GetReviewByIdQuery {
        if (reviewId == null || reviewId <= 0) {
            throw new IllegalArgumentException("Review ID cannot be null or less than or equal to 0");
        }
    }
}