package com.textilflow.platform.reviews.domain.model.queries;

/**
 * Query to check if a businessman has already reviewed a specific supplier
 * Used by frontend method: hasBusinessmanReviewed(supplierId, businessmanId, callback)
 * @param supplierId the supplier ID to check
 * @param businessmanId the businessman ID to check
 */
public record CheckIfBusinessmanReviewedSupplierQuery(Long supplierId, Long businessmanId) {

    /**
     * Constructor with validation
     */
    public CheckIfBusinessmanReviewedSupplierQuery {
        if (supplierId == null || supplierId <= 0) {
            throw new IllegalArgumentException("Supplier ID cannot be null or less than or equal to 0");
        }
        if (businessmanId == null || businessmanId <= 0) {
            throw new IllegalArgumentException("Businessman ID cannot be null or less than or equal to 0");
        }
    }
}