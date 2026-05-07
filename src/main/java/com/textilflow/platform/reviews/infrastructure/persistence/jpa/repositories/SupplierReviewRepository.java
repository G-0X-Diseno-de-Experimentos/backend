package com.textilflow.platform.reviews.infrastructure.persistence.jpa.repositories;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.SupplierId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SupplierReviewRepository
 * Repository interface para manejar la persistencia de las reseñas de suppliers
 * Siguiendo DDD estricto - solo operaciones de persistencia, sin lógica de negocio
 */
@Repository
public interface SupplierReviewRepository extends JpaRepository<SupplierReview, Long> {

    /**
     * Encuentra todas las reseñas para un supplier específico
     * Usado por: GetReviewsBySupplierIdQuery
     *
     * @param supplierId El Value Object SupplierId del supplier
     * @return Lista de reseñas para el supplier
     */
    List<SupplierReview> findBySupplierId(SupplierId supplierId);

    /**
     * Encuentra una reseña específica por supplier y businessman
     * Usado para verificar si ya existe una reseña y para actualizaciones
     *
     * @param supplierId El Value Object SupplierId del supplier
     * @param businessmanId El Value Object BusinessmanId del businessman
     * @return Optional con la reseña si existe
     */
    Optional<SupplierReview> findBySupplierIdAndBusinessmanId(SupplierId supplierId, BusinessmanId businessmanId);

    /**
     * Verifica si existe una reseña de un businessman para un supplier
     * Usado por: CheckIfBusinessmanReviewedSupplierQuery
     *
     * @param supplierId El Value Object SupplierId del supplier
     * @param businessmanId El Value Object BusinessmanId del businessman
     * @return true si existe la reseña, false en caso contrario
     */
    boolean existsBySupplierIdAndBusinessmanId(SupplierId supplierId, BusinessmanId businessmanId);

    /**
     * Cuenta el número total de reseñas para un supplier
     * Útil para estadísticas y cálculo de ratings promedio
     *
     * @param supplierId El Value Object SupplierId del supplier
     * @return Número de reseñas para el supplier
     */
    long countBySupplierId(SupplierId supplierId);

    /**
     * Encuentra todas las reseñas de un businessman específico
     * Útil para obtener el historial de reseñas de un businessman
     *
     * @param businessmanId El Value Object BusinessmanId del businessman
     * @return Lista de reseñas del businessman
     */
    List<SupplierReview> findByBusinessmanId(BusinessmanId businessmanId);
}