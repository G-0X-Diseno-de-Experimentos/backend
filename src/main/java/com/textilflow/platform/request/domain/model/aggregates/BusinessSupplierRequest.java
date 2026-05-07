package com.textilflow.platform.request.domain.model.aggregates;

import com.textilflow.platform.request.domain.model.valueobjects.*;
import com.textilflow.platform.request.domain.model.events.RequestCreatedEvent;
import com.textilflow.platform.request.domain.model.events.RequestStatusUpdatedEvent;
import com.textilflow.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "business_supplier_requests")
@NoArgsConstructor
@Getter
public class BusinessSupplierRequest extends AuditableAbstractAggregateRoot<BusinessSupplierRequest> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "businessman_id"))
    private BusinessmanId businessmanId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "supplier_id"))
    private SupplierId supplierId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "batch_type"))
    private BatchType batchType;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "color"))
    private Color color;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "quantity"))
    private Quantity quantity;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "address"))
    private Address address;

    public BusinessSupplierRequest(BusinessmanId businessmanId, SupplierId supplierId,
                                   String message, BatchType batchType, Color color,
                                   Quantity quantity, Address address) {
        this.businessmanId = businessmanId;
        this.supplierId = supplierId;
        this.status = RequestStatus.PENDING;
        this.message = message;
        this.batchType = batchType;
        this.color = color;
        this.quantity = quantity;
        this.address = address;

        registerEvent(new RequestCreatedEvent(this));
    }

    public void updateStatus(RequestStatus status, String message) {
        this.status = status;
        this.message = message;
        registerEvent(new RequestStatusUpdatedEvent(this));
    }

    public void updateRequestDetails(String message, BatchType batchType, Color color,
                                     Quantity quantity, Address address) {
        this.message = message;
        this.batchType = batchType;
        this.color = color;
        this.quantity = quantity;
        this.address = address;
    }

    public boolean isPending() {
        return status == RequestStatus.PENDING;
    }

    public boolean isAccepted() {
        return status == RequestStatus.ACCEPTED;
    }

    public boolean isRejected() {
        return status == RequestStatus.REJECTED;
    }

    public boolean isCancelled() {
        return status == RequestStatus.CANCELLED;
    }
}
