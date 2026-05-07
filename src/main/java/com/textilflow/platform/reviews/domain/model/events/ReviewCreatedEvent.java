package com.textilflow.platform.reviews.domain.model.events;

import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.Rating;
import com.textilflow.platform.reviews.domain.model.valueobjects.SupplierId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * ReviewCreatedEvent
 * Evento de dominio que se dispara cuando se crea una nueva reseña de supplier
 */
@Getter
public class ReviewCreatedEvent extends ApplicationEvent {

    private final Long reviewId;
    private final SupplierId supplierId;
    private final BusinessmanId businessmanId;
    private final Rating rating;

    /**
     * Constructor del evento ReviewCreatedEvent
     * @param source El objeto fuente del evento (típicamente el agregado SupplierReview)
     * @param reviewId El ID de la reseña creada
     * @param supplierId El ID del supplier que recibió la reseña
     * @param businessmanId El ID del businessman que creó la reseña
     * @param rating La calificación otorgada
     */
    public ReviewCreatedEvent(Object source, Long reviewId, SupplierId supplierId,
                              BusinessmanId businessmanId, Rating rating) {
        super(source);
        this.reviewId = reviewId;
        this.supplierId = supplierId;
        this.businessmanId = businessmanId;
        this.rating = rating;
    }

    /**
     * Obtiene el valor del supplier ID
     */
    public Long getSupplierIdValue() {
        return supplierId.supplierId();
    }

    /**
     * Obtiene el valor del businessman ID
     */
    public Long getBusinessmanIdValue() {
        return businessmanId.businessmanId();
    }

    /**
     * Obtiene el valor del rating
     */
    public Integer getRatingValue() {
        return rating.value();
    }
}