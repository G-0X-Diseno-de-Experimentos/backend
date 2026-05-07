package com.textilflow.platform.request.domain.services;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import com.textilflow.platform.request.domain.model.commands.*;

import java.util.Optional;

public interface RequestCommandService {
    Long handle(CreateBusinessSupplierRequestCommand command);
    Optional<BusinessSupplierRequest> handle(UpdateRequestStatusCommand command);
    Optional<BusinessSupplierRequest> handle(UpdateRequestDetailsCommand command);
    void handle(DeleteRequestCommand command);
}
