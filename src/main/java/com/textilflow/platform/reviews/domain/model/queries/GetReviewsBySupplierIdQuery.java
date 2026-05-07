package com.textilflow.platform.reviews.domain.model.queries;

/**
 * Query to get all reviews for a specific supplier
 * @param supplierId the supplier ID to get reviews for
 */
public record GetReviewsBySupplierIdQuery(Long supplierId) {

    /**
     * Constructor with validation
     */
    public GetReviewsBySupplierIdQuery {
        if (supplierId == null || supplierId <= 0) {
            throw new IllegalArgumentException("Supplier ID cannot be null or less than or equal to 0");
        }
    }
}