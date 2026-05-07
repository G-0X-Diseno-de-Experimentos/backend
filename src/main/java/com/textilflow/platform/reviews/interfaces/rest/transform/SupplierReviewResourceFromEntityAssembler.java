package com.textilflow.platform.reviews.interfaces.rest.transform;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.interfaces.rest.resources.SupplierReviewResource;

/**
 * SupplierReviewResourceFromEntityAssembler
 * Assembler para convertir SupplierReview entity a SupplierReviewResource
 * Siguiendo DDD estricto - transformaciones explícitas entre capas
 */
public class SupplierReviewResourceFromEntityAssembler {

    /**
     * Convierte un SupplierReview entity a SupplierReviewResource
     *
     * @param entity El {@link SupplierReview} entity a convertir
     * @return El {@link SupplierReviewResource} resource resultante de la conversión
     */
    public static SupplierReviewResource toResourceFromEntity(SupplierReview entity) {
        return new SupplierReviewResource(
                entity.getId(),
                entity.getSupplierIdValue(),
                entity.getBusinessmanIdValue(),
                entity.getRatingValue(),
                entity.getReviewContentValue(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}