package com.textilflow.platform.request.domain.services;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import com.textilflow.platform.request.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface RequestQueryService {
    Optional<BusinessSupplierRequest> handle(GetRequestByIdQuery query);
    List<BusinessSupplierRequest> handle(GetRequestsByBusinessmanIdQuery query);
    List<BusinessSupplierRequest> handle(GetRequestsBySupplierIdQuery query);
    List<BusinessSupplierRequest> handle(GetAllRequestsQuery query);
}
