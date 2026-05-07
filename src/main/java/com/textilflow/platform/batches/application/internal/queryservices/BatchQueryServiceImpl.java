package com.textilflow.platform.batches.application.internal.queryservices;

import com.textilflow.platform.batches.domain.model.aggregates.Batch;
import com.textilflow.platform.batches.domain.model.queries.GetAllBatchesQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchByIdQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchesBySupplierIdQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchesByBusinessmanIdQuery;
import com.textilflow.platform.batches.domain.services.BatchQueryService;
import com.textilflow.platform.batches.infraestructure.persistence.repositories.BatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchQueryServiceImpl implements BatchQueryService {
    private final BatchRepository batchRepository;

    public BatchQueryServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Override
    public Optional<Batch> handle(GetBatchByIdQuery query) {
        return batchRepository.findById(query.batchId());
    }

    @Override
    public List<Batch> handle(GetAllBatchesQuery query) {
        return batchRepository.findAll();
    }

    @Override
    public List<Batch> handle(GetBatchesBySupplierIdQuery query) {
        return batchRepository.findBySupplierId(query.supplierId());
    }

    @Override
    public List<Batch> handle(GetBatchesByBusinessmanIdQuery query) {
        return batchRepository.findByBusinessmanId(query.businessmanId());
    }




}
