package com.textilflow.platform.observation.infrastructure.cloudinary;

import com.textilflow.platform.observation.domain.services.ObservationImageService;
import com.textilflow.platform.shared.application.services.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Observation-specific Cloudinary service that adapts shared CloudinaryService
 */
@Service
public class ObservationCloudinaryService implements ObservationImageService {

    private final CloudinaryService cloudinaryService;
    private static final String OBSERVATIONS_FOLDER = "textilflow/observations";

    public ObservationCloudinaryService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public String uploadImage(Long observationId, MultipartFile file) {
        String folder = OBSERVATIONS_FOLDER;
        String publicId = generatePublicId(observationId);

        return cloudinaryService.uploadImage(file, folder, publicId);
    }

    @Override
    public void deleteImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            String publicId = cloudinaryService.extractPublicId(imageUrl);
            if (publicId != null) {
                cloudinaryService.deleteImage(publicId);
            }
        }
    }

    private String generatePublicId(Long observationId) {
        return String.format("observation_%d_image", observationId);
    }
}
