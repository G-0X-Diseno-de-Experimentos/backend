package com.textilflow.platform.reviews.application.internal.commandservices;

import com.textilflow.platform.reviews.domain.model.aggregates.SupplierReview;
import com.textilflow.platform.reviews.domain.model.commands.CreateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.commands.UpdateSupplierReviewCommand;
import com.textilflow.platform.reviews.domain.model.valueobjects.BusinessmanId;
import com.textilflow.platform.reviews.domain.model.valueobjects.SupplierId;
import com.textilflow.platform.reviews.domain.services.SupplierReviewCommandService;
import com.textilflow.platform.reviews.infrastructure.persistence.jpa.repositories.SupplierReviewRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * SupplierReviewCommandServiceImpl
 * Implementación del service para manejar comandos de reseñas de suppliers
 * Siguiendo DDD estricto - coordina operaciones pero no contiene lógica de negocio
 */
@Service
public class SupplierReviewCommandServiceImpl implements SupplierReviewCommandService {

    private final SupplierReviewRepository supplierReviewRepository;

    /**
     * Constructor
     * @param supplierReviewRepository El repository para persistencia de reseñas
     */
    public SupplierReviewCommandServiceImpl(SupplierReviewRepository supplierReviewRepository) {
        this.supplierReviewRepository = supplierReviewRepository;
    }

    /**
     * Handle CreateSupplierReviewCommand
     * Crea una nueva reseña verificando que no exista una previa del mismo businessman
     */
    @Override
    public Optional<SupplierReview> handle(CreateSupplierReviewCommand command) {
        var supplierId = new SupplierId(command.supplierId());
        var businessmanId = new BusinessmanId(command.businessmanId());

        // Verificar que no exista una reseña previa del mismo businessman para este supplier
        if (supplierReviewRepository.existsBySupplierIdAndBusinessmanId(supplierId, businessmanId)) {
            throw new IllegalArgumentException(
                    String.format("Businessman with ID %d has already reviewed supplier with ID %d",
                            command.businessmanId(), command.supplierId())
            );
        }

        // Crear la nueva reseña - el agregado maneja la lógica de negocio y eventos
        var supplierReview = new SupplierReview(command);

        try {
            var savedReview = supplierReviewRepository.save(supplierReview);
            return Optional.of(savedReview);
        } catch (Exception e) {
            throw new RuntimeException("Error saving supplier review: " + e.getMessage(), e);
        }
    }

    /**
     * Handle UpdateSupplierReviewCommand
     * Actualiza una reseña existente verificando que existe y pertenece al businessman
     */
    @Override
    public Optional<SupplierReview> handle(UpdateSupplierReviewCommand command) {
        // Buscar la reseña existente
        var existingReview = supplierReviewRepository.findById(command.reviewId());

        if (existingReview.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Review with ID %d not found", command.reviewId())
            );
        }

        var review = existingReview.get();

        // El agregado maneja la actualización y dispara eventos de dominio
        review.update(command);

        try {
            var updatedReview = supplierReviewRepository.save(review);
            return Optional.of(updatedReview);
        } catch (Exception e) {
            throw new RuntimeException("Error updating supplier review: " + e.getMessage(), e);
        }
    }
}