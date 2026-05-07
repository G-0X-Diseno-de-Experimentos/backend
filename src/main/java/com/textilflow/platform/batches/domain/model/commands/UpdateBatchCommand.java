package com.textilflow.platform.batches.domain.model.commands;

import com.textilflow.platform.batches.domain.model.valueobjects.BatchStatus;

import java.time.LocalDate;

public record UpdateBatchCommand(Long batchId, String code, String client, Long businessmanId, Long supplierId,
                                 String fabricType, String color, Integer quantity, Double price,
                                 String observations, String address, LocalDate date, BatchStatus status, String imageUrl) {

    public UpdateBatchCommand {
        if (batchId == null || batchId <= 0) {
            throw new IllegalArgumentException("Batch ID must be a positive number");
        }
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        if (client == null || client.isEmpty()) {
            throw new IllegalArgumentException("Client cannot be null or empty");
        }
        if (businessmanId == null || businessmanId <= 0) {
            throw new IllegalArgumentException("Businessman ID must be a positive number");
        }
        if (supplierId == null || supplierId <= 0) {
            throw new IllegalArgumentException("Supplier ID must be a positive number");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
    }
}
