package com.textilflow.platform.observation.infrastructure.persistence.jpa.repositories;

import com.textilflow.platform.observation.domain.model.aggregates.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findByBatchId(Long batchId);
    List<Observation> findByBusinessmanId(Long businessmanId);
    List<Observation> findBySupplierId(Long supplierId);
    boolean existsByBatchId(Long batchId);
    List<Observation> findByBatchIdOrderByCreatedAtDesc(Long batchId);
}
