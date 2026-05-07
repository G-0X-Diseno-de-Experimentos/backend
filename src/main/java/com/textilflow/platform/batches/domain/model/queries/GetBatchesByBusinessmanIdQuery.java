package com.textilflow.platform.batches.domain.model.queries;

public record GetBatchesByBusinessmanIdQuery(Long businessmanId) {
    public GetBatchesByBusinessmanIdQuery {
        if (businessmanId == null || businessmanId <= 0) {
            throw new IllegalArgumentException("Businessman ID must be a positive number");
        }
    }
}