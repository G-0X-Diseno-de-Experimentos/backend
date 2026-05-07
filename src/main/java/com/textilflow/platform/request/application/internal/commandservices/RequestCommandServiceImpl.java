package com.textilflow.platform.request.application.internal.commandservices;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import com.textilflow.platform.request.domain.model.commands.*;
import com.textilflow.platform.request.domain.model.valueobjects.*;
import com.textilflow.platform.request.domain.services.RequestCommandService;
import com.textilflow.platform.request.infrastructure.persistence.jpa.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestCommandServiceImpl implements RequestCommandService {

    private final RequestRepository requestRepository;

    public RequestCommandServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Long handle(CreateBusinessSupplierRequestCommand command) {
        var businessmanId = new BusinessmanId(command.businessmanId());
        var supplierId = new SupplierId(command.supplierId());
        var batchType = new BatchType(command.batchType());
        var color = new Color(command.color());
        var quantity = new Quantity(command.quantity());
        var address = new Address(command.address());

        var request = new BusinessSupplierRequest(businessmanId, supplierId,
                command.message(), batchType,
                color, quantity, address);

        requestRepository.save(request);
        return request.getId();
    }

    @Override
    public Optional<BusinessSupplierRequest> handle(UpdateRequestStatusCommand command) {
        return requestRepository.findById(command.requestId())
                .map(request -> {
                    request.updateStatus(command.status(), command.message());
                    return requestRepository.save(request);
                });
    }

    @Override
    public Optional<BusinessSupplierRequest> handle(UpdateRequestDetailsCommand command) {
        return requestRepository.findById(command.requestId())
                .map(request -> {
                    var batchType = new BatchType(command.batchType());
                    var color = new Color(command.color());
                    var quantity = new Quantity(command.quantity());
                    var address = new Address(command.address());

                    request.updateRequestDetails(command.message(), batchType,
                            color, quantity, address);
                    return requestRepository.save(request);
                });
    }

    @Override
    public void handle(DeleteRequestCommand command) {
        requestRepository.deleteById(command.requestId());
    }
}
