package com.textilflow.platform.batches.domain.model.commands;

public record UpdateBatchImageCommand(Long batchId, String imageUrl) {
    public UpdateBatchImageCommand {
        if (batchId == null || batchId <= 0) {
            throw new IllegalArgumentException("Batch ID cannot be null or less than or equal to 0");
        }
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty");
        }
    }
}