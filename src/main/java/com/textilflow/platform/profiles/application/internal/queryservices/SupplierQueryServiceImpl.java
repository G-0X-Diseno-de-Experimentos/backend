package com.textilflow.platform.profiles.application.internal.queryservices;

import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import com.textilflow.platform.profiles.domain.model.queries.GetAllSuppliersQuery;
import com.textilflow.platform.profiles.domain.model.queries.GetSupplierByUserIdQuery;
import com.textilflow.platform.profiles.domain.services.SupplierQueryService;
import com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Supplier query service implementation
 */
@Service
public class SupplierQueryServiceImpl implements SupplierQueryService {

    private final SupplierRepository supplierRepository;

    public SupplierQueryServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Optional<Supplier> handle(GetSupplierByUserIdQuery query) {
        return supplierRepository.findByUserId(query.userId());
    }

    @Override
    public List<Supplier> handle(GetAllSuppliersQuery query) {
        return supplierRepository.findAll();
    }
}