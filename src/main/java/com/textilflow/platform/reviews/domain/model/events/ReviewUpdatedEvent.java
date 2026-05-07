package com.textilflow.platform.reviews.domain.model.events;

import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.Rating;
import com.textilflow.platform.reviews.domain.model.valueobjects.SupplierId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * ReviewUpdatedEvent
 * Evento de dominio que se dispara cuando se actualiza una reseña de supplier
 */
@Getter
public class ReviewUpdatedEvent extends ApplicationEvent {

    private final Long reviewId;
    private final SupplierId supplierId;
    private final BusinessmanId businessmanId;
    private final Rating oldRating;
    private final Rating newRating;

    /**
     * Constructor del evento ReviewUpdatedEvent
     * @param source El objeto fuente del evento (típicamente el agregado SupplierReview)
     * @param reviewId El ID de la reseña actualizada
     * @param supplierId El ID del supplier que recibió la reseña
     * @param businessmanId El ID del businessman que actualizó la reseña
     * @param oldRating La calificación anterior
     * @param newRating La nueva calificación
     */
    public ReviewUpdatedEvent(Object source, Long reviewId, SupplierId supplierId,
                              BusinessmanId businessmanId, Rating oldRating, Rating newRating) {
        super(source);
        this.reviewId = reviewId;
        this.supplierId = supplierId;
        this.businessmanId = businessmanId;
        this.oldRating = oldRating;
        this.newRating = newRating;
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
     * Obtiene el valor del rating anterior
     */
    public Integer getOldRatingValue() {
        return oldRating.value();
    }

    /**
     * Obtiene el valor del nuevo rating
     */
    public Integer getNewRatingValue() {
        return newRating.value();
    }

    /**
     * Verifica si el rating cambió
     */
    public boolean hasRatingChanged() {
        return !oldRating.equals(newRating);
    }
}