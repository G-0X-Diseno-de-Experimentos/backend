package com.textilflow.platform.request.interfaces.rest.transform;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import com.textilflow.platform.request.interfaces.rest.resources.BusinessSupplierRequestResource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class BusinessSupplierRequestResourceFromEntityAssembler {

    public static BusinessSupplierRequestResource toResourceFromEntity(BusinessSupplierRequest entity) {
        return new BusinessSupplierRequestResource(
                entity.getId(),
                entity.getBusinessmanId().value(),
                entity.getSupplierId().value(),
                entity.getStatus(),
                entity.getMessage(),
                entity.getBatchType().value(),
                entity.getColor().value(),
                entity.getQuantity().value(),
                entity.getAddress().value(),
                convertToLocalDateTime(entity.getCreatedAt()),
                convertToLocalDateTime(entity.getUpdatedAt())
        );
    }

    private static LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}