package com.textilflow.platform.batches.domain.model.commands;

public record DeleteBatchImageCommand(Long batchId) {
    public DeleteBatchImageCommand {
        if (batchId == null || batchId <= 0) {
            throw new IllegalArgumentException("Batch ID cannot be null or less than or equal to 0");
        }
    }
}