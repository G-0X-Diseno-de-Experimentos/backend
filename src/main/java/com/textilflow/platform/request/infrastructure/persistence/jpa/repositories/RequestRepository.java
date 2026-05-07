package com.textilflow.platform.request.infrastructure.persistence.jpa.repositories;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<BusinessSupplierRequest, Long> {

    @Query("SELECT r FROM BusinessSupplierRequest r WHERE r.businessmanId.value = :businessmanId")
    List<BusinessSupplierRequest> findByBusinessmanId(@Param("businessmanId") Long businessmanId);

    @Query("SELECT r FROM BusinessSupplierRequest r WHERE r.supplierId.value = :supplierId")
    List<BusinessSupplierRequest> findBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT r FROM BusinessSupplierRequest r ORDER BY r.createdAt DESC")
    List<BusinessSupplierRequest> findAllOrderByCreatedAtDesc();
}
