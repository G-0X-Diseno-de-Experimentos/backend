package com.textilflow.platform.batches.domain.services;

import com.textilflow.platform.batches.domain.model.queries.GetAllBatchesQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchByIdQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchesBySupplierIdQuery;
import com.textilflow.platform.batches.domain.model.queries.GetBatchesByBusinessmanIdQuery;
import com.textilflow.platform.batches.domain.model.aggregates.Batch;

import java.util.List;
import java.util.Optional;

public interface BatchQueryService {

    Optional<Batch> handle(GetBatchByIdQuery query);

    List<Batch> handle(GetAllBatchesQuery query);
    
    List<Batch> handle(GetBatchesBySupplierIdQuery query);
    
    List<Batch> handle(GetBatchesByBusinessmanIdQuery query);
}
