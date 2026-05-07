package com.textilflow.platform.request.domain.model.events;

import com.textilflow.platform.request.domain.model.aggregates.BusinessSupplierRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public final class RequestStatusUpdatedEvent extends ApplicationEvent {

    private final BusinessSupplierRequest request;

    public RequestStatusUpdatedEvent(BusinessSupplierRequest request) {
        super(request);
        this.request = request;
    }
}
