package com.textilflow.platform.reviews.domain.model.aggregates;

import com.textilflow.platform.reviews.domain.model.commands.CreateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.commands.UpdateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.events.ReviewCreatedEvent;
import com.textilflow.platform.reviews.domain.model.events.ReviewUpdatedEvent;
import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.Rating;
import com.textilflow.platform.reviews.domain.model.valueobjects.ReviewContent;
import com.textilflow.platform.reviews.domain.model.valueobjects.SupplierId;
import com.textilflow.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;

/**
 * SupplierReview Aggregate Root
 * Representa una reseña que un businessman deja a un supplier
 */
@Entity
public class SupplierReview extends AuditableAbstractAggregateRoot<SupplierReview> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "supplierId", column = @Column(name = "supplier_id"))
    })
    private SupplierId supplierId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "businessmanId", column = @Column(name = "businessman_id"))
    })
    private BusinessmanId businessmanId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "rating"))
    })
    private Rating rating;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "content", column = @Column(name = "review_content"))
    })
    private ReviewContent reviewContent;

    /**
     * Default constructor for JPA
     */
    public SupplierReview() {
        super();
    }

    /**
     * Constructor con CreateSupplierReviewCommand
     * @param command The {@link CreateSupplierReviewCommand} instance
     */
    public SupplierReview(CreateSupplierReviewCommand command) {
        this();
        this.supplierId = new SupplierId(command.supplierId());
        this.businessmanId = new BusinessmanId(command.businessmanId());
        this.rating = new Rating(command.rating());
        this.reviewContent = new ReviewContent(command.reviewContent());

        // Registrar evento de dominio
        this.addDomainEvent(new ReviewCreatedEvent(this, this.getId(), this.supplierId, this.businessmanId, this.rating));
    }

    /**
     * Constructor completo
     */
    public SupplierReview(Long supplierId, Long businessmanId, Integer rating, String reviewContent) {
        this();
        this.supplierId = new SupplierId(supplierId);
        this.businessmanId = new BusinessmanId(businessmanId);
        this.rating = new Rating(rating);
        this.reviewContent = new ReviewContent(reviewContent);

        // Registrar evento de dominio
        this.addDomainEvent(new ReviewCreatedEvent(this, this.getId(), this.supplierId, this.businessmanId, this.rating));
    }

    /**
     * Actualiza la reseña con nueva información
     * @param command The {@link UpdateSupplierReviewCommand} instance
     */
    public void update(UpdateSupplierReviewCommand command) {
        var oldRating = this.rating;
        this.rating = new Rating(command.rating());
        this.reviewContent = new ReviewContent(command.reviewContent());

        // Registrar evento de dominio
        this.addDomainEvent(new ReviewUpdatedEvent(this, this.getId(), this.supplierId, this.businessmanId, oldRating, this.rating));
    }

    /**
     * Actualiza rating y contenido directamente
     */
    public void update(Integer newRating, String newReviewContent) {
        var oldRating = this.rating;
        this.rating = new Rating(newRating);
        this.reviewContent = new ReviewContent(newReviewContent);

        // Registrar evento de dominio
        this.addDomainEvent(new ReviewUpdatedEvent(this, this.getId(), this.supplierId, this.businessmanId, oldRating, this.rating));
    }

    /**
     * Verifica si esta reseña pertenece al businessman especificado
     */
    public boolean belongsToBusinessman(BusinessmanId businessmanId) {
        return this.businessmanId.equals(businessmanId);
    }

    /**
     * Verifica si esta reseña es para el supplier especificado
     */
    public boolean isForSupplier(SupplierId supplierId) {
        return this.supplierId.equals(supplierId);
    }

    // Getters
    public SupplierId getSupplierId() {
        return supplierId;
    }

    public BusinessmanId getBusinessmanId() {
        return businessmanId;
    }

    public Rating getRating() {
        return rating;
    }

    public ReviewContent getReviewContent() {
        return reviewContent;
    }

    public Long getSupplierIdValue() {
        return supplierId.supplierId();
    }

    public Long getBusinessmanIdValue() {
        return businessmanId.businessmanId();
    }

    public Integer getRatingValue() {
        return rating.value();
    }

    public String getReviewContentValue() {
        return reviewContent.content();
    }
}