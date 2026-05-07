package com.textilflow.platform.observation.domain.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Observation image service interface
 */
public interface ObservationImageService {

    /**
     * Upload observation image
     */
    String uploadImage(Long observationId, MultipartFile file);

    /**
     * Delete observation image
     */
    void deleteImage(String imageUrl);
}
