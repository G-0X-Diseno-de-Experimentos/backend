package com.textilflow.platform.reviews.application.internal.queryservices;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.domain.model.queries.CheckIfBusinessmanReviewedSupplierQuery;
import com.textilflow.platform.reviews.domain.model.queries.GetReviewByIdQuery;
import com.textilflow.platform.reviews.domain.model.queries.GetReviewsBySupplierIdQuery;
import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.SupplierId;
import com.textilflow.platform.reviews.domain.services.SupplierReviewQueryService;
import com.textilflow.platform.reviews.infrastructure.persistence.jpa.repositories.SupplierReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * SupplierReviewQueryServiceImpl
 * Implementación del service para manejar queries de reseñas de suppliers
 * Siguiendo DDD estricto y CQRS - solo operaciones de consulta
 */
@Service
public class SupplierReviewQueryServiceImpl implements SupplierReviewQueryService {

    private final SupplierReviewRepository supplierReviewRepository;

    /**
     * Constructor
     * @param supplierReviewRepository El repository para consultas de reseñas
     */
    public SupplierReviewQueryServiceImpl(SupplierReviewRepository supplierReviewRepository) {
        this.supplierReviewRepository = supplierReviewRepository;
    }

    /**
     * Handle GetReviewsBySupplierIdQuery
     * Obtiene todas las reseñas para un supplier específico
     */
    @Override
    public List<SupplierReview> handle(GetReviewsBySupplierIdQuery query) {
        var supplierId = new SupplierId(query.supplierId());
        return supplierReviewRepository.findBySupplierId(supplierId);
    }

    /**
     * Handle GetReviewByIdQuery
     * Obtiene una reseña específica por su ID
     */
    @Override
    public Optional<SupplierReview> handle(GetReviewByIdQuery query) {
        return supplierReviewRepository.findById(query.reviewId());
    }

    /**
     * Handle CheckIfBusinessmanReviewedSupplierQuery
     * Verifica si un businessman ya ha dejado una reseña para un supplier
     * Usado por el endpoint: GET /api/v1/supplier-reviews/check/{supplierId}/{businessmanId}
     */
    @Override
    public boolean handle(CheckIfBusinessmanReviewedSupplierQuery query) {
        var supplierId = new SupplierId(query.supplierId());
        var businessmanId = new BusinessmanId(query.businessmanId());

        return supplierReviewRepository.existsBySupplierIdAndBusinessmanId(supplierId, businessmanId);
    }
}