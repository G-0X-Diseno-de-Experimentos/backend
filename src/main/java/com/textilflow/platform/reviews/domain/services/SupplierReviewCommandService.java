package com.textilflow.platform.reviews.domain.services;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.domain.model.commands.CreateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.commands.UpdateSupplierReviewCommand;

import java.util.Optional;

/**
 * SupplierReviewCommandService
 * Service contract para manejar comandos relacionados con las reseñas de suppliers
 */
public interface SupplierReviewCommandService {

    /**
     * Handle CreateSupplierReviewCommand
     * Crea una nueva reseña de supplier
     *
     * @param command El comando {@link CreateSupplierReviewCommand} con los datos de la reseña
     * @return Un {@link Optional} con la reseña creada si es exitoso, empty si falla
     * @throws IllegalArgumentException si ya existe una reseña del businessman para el supplier
     * @throws IllegalArgumentException si los IDs no son válidos
     */
    Optional<SupplierReview> handle(CreateSupplierReviewCommand command);

    /**
     * Handle UpdateSupplierReviewCommand
     * Actualiza una reseña existente de supplier
     *
     * @param command El comando {@link UpdateSupplierReviewCommand} con los nuevos datos
     * @return Un {@link Optional} con la reseña actualizada si es exitoso, empty si no existe
     * @throws IllegalArgumentException si la reseña no existe
     * @throws IllegalArgumentException si los datos no son válidos
     */
    Optional<SupplierReview> handle(UpdateSupplierReviewCommand command);
}