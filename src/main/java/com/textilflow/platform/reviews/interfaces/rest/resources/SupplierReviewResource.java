package com.textilflow.platform.reviews.interfaces.rest.resources;

import java.util.Date;

/**
 * SupplierReviewResource
 * Resource (DTO) para representar una reseña de supplier en las respuestas
 * Contiene toda la información necesaria para el frontend
 */
public record SupplierReviewResource(
        Long id,
        Long supplierId,
        Long businessmanId,
        Integer rating,
        String reviewContent,
        Date createdAt,
        Date updatedAt
) {

    /**
     * Constructor sin validaciones ya que es solo para respuestas
     * Los datos vienen del agregado que ya está validado
     */
}