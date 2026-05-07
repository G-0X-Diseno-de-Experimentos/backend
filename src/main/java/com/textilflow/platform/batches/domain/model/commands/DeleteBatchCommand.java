package com.textilflow.platform.batches.domain.model.commands;

public record DeleteBatchCommand(Long batchId) {

    public DeleteBatchCommand {
        if(batchId == null || batchId <= 0) {
            throw new IllegalArgumentException("Batch ID must be greater than 0");
        }
    }

}
