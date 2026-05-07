package com.textilflow.platform.batches.infraestructure.persistence.cloudinary;

import com.textilflow.platform.shared.application.services.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BatchCloudinaryService {

    private final CloudinaryService cloudinaryService;
    private static final String BATCHES_FOLDER = "textilflow/batches";

    public BatchCloudinaryService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    public String uploadBatchImage(MultipartFile file) {
        return cloudinaryService.uploadImage(file, BATCHES_FOLDER);
    }

    public void deleteBatchImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            String publicId = cloudinaryService.extractPublicId(imageUrl);
            if (publicId != null) {
                cloudinaryService.deleteImage(publicId);
            }
        }
    }
}
