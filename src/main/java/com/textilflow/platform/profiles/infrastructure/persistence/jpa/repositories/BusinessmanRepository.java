package com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Businessman repository interface
 */
@Repository
public interface BusinessmanRepository extends JpaRepository<Businessman, Long> {

    /**
     * Find businessman by user ID
     */
    Optional<Businessman> findByUserId(Long userId);

    /**
     * Check if businessman exists by user ID
     */
    boolean existsByUserId(Long userId);

    /**
     * Delete businessman by user ID
     */
    void deleteByUserId(Long userId);
}
