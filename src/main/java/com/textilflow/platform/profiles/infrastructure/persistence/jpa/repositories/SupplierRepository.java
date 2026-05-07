package com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Supplier repository interface
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    /**
     * Find supplier by user ID
     */
    Optional<Supplier> findByUserId(Long userId);

    /**
     * Check if supplier exists by user ID
     */
    boolean existsByUserId(Long userId);

    /**
     * Delete supplier by user ID
     */
    void deleteByUserId(Long userId);
}
