package com.textilflow.platform.reviews.interfaces.rest.transform;

import com.textilflow.platform.reviews.domain.model.commands.UpdateSupplierReviewCommand;
import com.textilflow.platform.reviews.interfaces.rest.resources.UpdateSupplierReviewResource;

/**
 * UpdateSupplierReviewCommandFromResourceAssembler
 * Assembler para convertir UpdateSupplierReviewResource a UpdateSupplierReviewCommand
 * Siguiendo DDD estricto - transformaciones explícitas entre capas
 */
public class UpdateSupplierReviewCommandFromResourceAssembler {

    /**
     * Convierte un UpdateSupplierReviewResource a UpdateSupplierReviewCommand
     *
     * @param reviewId El ID de la reseña a actualizar (viene del path parameter)
     * @param resource El {@link UpdateSupplierReviewResource} resource a convertir
     * @return El {@link UpdateSupplierReviewCommand} command resultante de la conversión
     */
    public static UpdateSupplierReviewCommand toCommandFromResource(Long reviewId, UpdateSupplierReviewResource resource) {
        return new UpdateSupplierReviewCommand(
                reviewId,
                resource.rating(),
                resource.reviewContent()
        );
    }
}