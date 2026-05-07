package com.textilflow.platform.observation.application.internal.queryservices;

import com.textilflow.platform.observation.domain.model.aggregates.Observation;
import com.textilflow.platform.observation.domain.model.queries.GetObservationByIdQuery;
import com.textilflow.platform.observation.domain.model.queries.GetObservationsByBatchIdQuery;
import com.textilflow.platform.observation.domain.model.queries.GetObservationsByBusinessmanIdQuery;
import com.textilflow.platform.observation.domain.model.queries.GetObservationsBySupplierIdQuery;
import com.textilflow.platform.observation.domain.services.ObservationQueryService;
import com.textilflow.platform.observation.infrastructure.persistence.jpa.repositories.ObservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObservationQueryServiceImpl implements ObservationQueryService {

    private final ObservationRepository observationRepository;

    public ObservationQueryServiceImpl(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    @Override
    public Optional<Observation> handle(GetObservationByIdQuery query) {
        return observationRepository.findById(query.observationId());
    }

    @Override
    public List<Observation> handle(GetObservationsByBatchIdQuery query) {
        return observationRepository.findByBatchId(query.batchId());
    }

    @Override
    public List<Observation> handle(GetObservationsByBusinessmanIdQuery query) {
        return observationRepository.findByBusinessmanId(query.businessmanId());
    }

    @Override
    public List<Observation> handle(GetObservationsBySupplierIdQuery query) {
        return observationRepository.findBySupplierId(query.supplierId());
    }
}
