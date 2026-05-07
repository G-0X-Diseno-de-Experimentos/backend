package com.textilflow.platform.batches.domain.model.commands;

import com.textilflow.platform.batches.domain.model.valueobjects.BatchStatus;

import java.time.LocalDate;

public record CreateBatchCommand(String code, String client, Long businessmanId,
                                 Long supplierId, String fabricType, String color, Double price,
                                 Integer quantity, String observations, String address,
                                 LocalDate date, BatchStatus status, String imageUrl) {

    public CreateBatchCommand {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        if (client == null || client.isEmpty()) {
            throw new IllegalArgumentException("Client cannot be null or empty");
        }
        if (businessmanId == null || businessmanId <= 0){
            throw new IllegalArgumentException("Businessman ID cannot be null or less than or equal to 0");
        }
        if (supplierId == null || supplierId <= 0) {
            throw new IllegalArgumentException("Supplier ID cannot be null or less than or equal to 0");
        }
        if (fabricType == null || fabricType.isEmpty()) {
            throw new IllegalArgumentException("Fabric type cannot be null or empty");
        }
        if (color == null || color.isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null or empty");
        }
        if (quantity == null || quantity <= 0){
            throw new IllegalArgumentException("Quantity cannot be null or less than or equal to 0");
        }
        if (observations == null || observations.isEmpty()) {
            throw new IllegalArgumentException("Observations cannot be null or empty");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be null or in the future");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Price cannot be null or less than or equal to 0");
        }
    }
}
