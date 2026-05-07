package com.textilflow.platform.reviews.interfaces.rest.transform;

import com.textilflow.platform.reviews.domain.model.commands.CreateSupplierReviewCommand;
import com.textilflow.platform.reviews.interfaces.rest.resources.CreateSupplierReviewResource;

/**
 * CreateSupplierReviewCommandFromResourceAssembler
 * Assembler para convertir CreateSupplierReviewResource a CreateSupplierReviewCommand
 * Siguiendo DDD estricto - transformaciones explícitas entre capas
 */
public class CreateSupplierReviewCommandFromResourceAssembler {

    /**
     * Convierte un CreateSupplierReviewResource a CreateSupplierReviewCommand
     *
     * @param resource El {@link CreateSupplierReviewResource} resource a convertir
     * @return El {@link CreateSupplierReviewCommand} command resultante de la conversión
     */
    public static CreateSupplierReviewCommand toCommandFromResource(CreateSupplierReviewResource resource) {
        return new CreateSupplierReviewCommand(
                resource.supplierId(),
                resource.businessmanId(),
                resource.rating(),
                resource.reviewContent()
        );
    }
}