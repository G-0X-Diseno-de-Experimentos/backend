package com.textilflow.platform.batches.infraestructure.persistence.repositories;

import com.textilflow.platform.batches.domain.model.aggregates.Batch;
import com.textilflow.platform.batches.domain.model.valueobjects.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    List<Batch> findByBusinessmanId(Long businessmanId);
    
    List<Batch> findBySupplierId(Long supplierId);
    
    List<Batch> findByStatus(BatchStatus status);
    
    List<Batch> findByClient(String client);
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdIsNot(String code, Long id);
    
    List<Batch> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Batch> findByBusinessmanIdAndStatus(Long businessmanId, BatchStatus status);
    
    List<Batch> findBySupplierIdAndStatus(Long supplierId, BatchStatus status);

}
