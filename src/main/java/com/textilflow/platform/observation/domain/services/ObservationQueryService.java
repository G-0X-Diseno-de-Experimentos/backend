package com.textilflow.platform.observation.domain.services;

import com.textilflow.platform.observation.domain.model.aggregates.Observation;
import com.textilflow.platform.observation.domain.model.queries.GetObservationByIdQuery;
import com.textilflow.platform.observation.domain.model.queries.GetObservationsByBatchIdQuery;
import com.textilflow.platform.observation.domain.model.queries.GetObservationsByBusinessmanIdQuery;
import com.textilflow.platform.observation.domain.model.queries.GetObservationsBySupplierIdQuery;

import java.util.List;
import java.util.Optional;

public interface ObservationQueryService {
    Optional<Observation> handle(GetObservationByIdQuery query);
    List<Observation> handle(GetObservationsByBatchIdQuery query);
    List<Observation> handle(GetObservationsByBusinessmanIdQuery query);
    List<Observation> handle(GetObservationsBySupplierIdQuery query);
}
