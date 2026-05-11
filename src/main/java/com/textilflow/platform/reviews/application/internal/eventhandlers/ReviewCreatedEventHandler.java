package com.textilflow.platform.reviews.application.internal.eventhandlers;

import com.textilflow.platform.reviews.domain.model.events.ReviewCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * ReviewCreatedEventHandler
 * Event handler que maneja el evento cuando se crea una nueva reseña de supplier
 * Siguiendo DDD - coordina efectos secundarios después de la creación de reseñas
 */
@Service
public class ReviewCreatedEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReviewCreatedEventHandler.class);

    // TODO: Inyectar services del contexto profiles cuando sea necesario
    // private final ExternalProfilesService externalProfilesService;

    /**
     * Constructor
     */
    public ReviewCreatedEventHandler() {
        // TODO: Inyectar dependencias cuando sean necesarias
    }

    /**
     * Maneja el evento ReviewCreatedEvent
     * Se ejecuta cuando se crea una nueva reseña
     *
     * @param event El evento ReviewCreatedEvent
     */
    @EventListener
    public void on(ReviewCreatedEvent event) {

        try {
            logger.info("Processing ReviewCreatedEvent for review ID: {}, supplier ID: {}, businessman ID: {}, rating: {}",
                    event.getReviewId(),
                    event.getSupplierIdValue(),
                    event.getBusinessmanIdValue(),
                    event.getRatingValue()
            );

            logger.info("Successfully processed ReviewCreatedEvent for review ID: {}", event.getReviewId());

        } catch (Exception e) {
            logger.error("Error processing ReviewCreatedEvent: {}", e.getMessage(), e);
        }
    }

    // TODO: Métodos privados para efectos secundarios
    // private void updateSupplierAverageRating(Long supplierId) {
    //     // Comunicación con contexto profiles vía ACL
    // }

    // private void sendNotificationToSupplier(Long supplierId, Long businessmanId) {
    //     // Envío de notificaciones
    // }
}