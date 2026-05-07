package com.textilflow.platform.batches.domain.model.queries;

public record GetBatchesBySupplierIdQuery(Long supplierId) {
    public GetBatchesBySupplierIdQuery {
        if (supplierId == null || supplierId <= 0) {
            throw new IllegalArgumentException("Supplier ID must be a positive number");
        }
    }
}