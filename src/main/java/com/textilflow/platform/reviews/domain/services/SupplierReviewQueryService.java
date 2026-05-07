package com.textilflow.platform.reviews.domain.services;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.domain.model.queries.CheckIfBusinessmanReviewedSupplierQuery;
import com.textilflow.platform.reviews.domain.model.queries.GetReviewByIdQuery;
import com.textilflow.platform.reviews.domain.model.queries.GetReviewsBySupplierIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * SupplierReviewQueryService
 * Service contract para manejar queries relacionadas con las reseñas de suppliers
 */
public interface SupplierReviewQueryService {

    /**
     * Handle GetReviewsBySupplierIdQuery
     * Obtiene todas las reseñas de un supplier específico
     *
     * @param query El query {@link GetReviewsBySupplierIdQuery} con el ID del supplier
     * @return Una lista de {@link SupplierReview} para el supplier especificado
     */
    List<SupplierReview> handle(GetReviewsBySupplierIdQuery query);

    /**
     * Handle GetReviewByIdQuery
     * Obtiene una reseña específica por su ID
     *
     * @param query El query {@link GetReviewByIdQuery} con el ID de la reseña
     * @return Un {@link Optional} con la reseña si existe, empty si no existe
     */
    Optional<SupplierReview> handle(GetReviewByIdQuery query);

    /**
     * Handle CheckIfBusinessmanReviewedSupplierQuery
     * Verifica si un businessman ya ha dejado una reseña para un supplier específico
     * Usado por el frontend: hasBusinessmanReviewed(supplierId, businessmanId, callback)
     *
     * @param query El query {@link CheckIfBusinessmanReviewedSupplierQuery} con los IDs
     * @return true si el businessman ya reseñó al supplier, false en caso contrario
     */
    boolean handle(CheckIfBusinessmanReviewedSupplierQuery query);
}