package com.textilflow.platform.request.application.internal.queryservices;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import com.textilflow.platform.request.domain.model.queries.*;
import com.textilflow.platform.request.domain.services.RequestQueryService;
import com.textilflow.platform.request.infrastructure.persistence.jpa.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestQueryServiceImpl implements RequestQueryService {

    private final RequestRepository requestRepository;

    public RequestQueryServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Optional<BusinessSupplierRequest> handle(GetRequestByIdQuery query) {
        return requestRepository.findById(query.requestId());
    }

    @Override
    public List<BusinessSupplierRequest> handle(GetRequestsByBusinessmanIdQuery query) {
        return requestRepository.findByBusinessmanId(query.businessmanId());
    }

    @Override
    public List<BusinessSupplierRequest> handle(GetRequestsBySupplierIdQuery query) {
        return requestRepository.findBySupplierId(query.supplierId());
    }

    @Override
    public List<BusinessSupplierRequest> handle(GetAllRequestsQuery query) {
        return requestRepository.findAllOrderByCreatedAtDesc();
    }
}
