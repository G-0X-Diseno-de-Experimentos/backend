package com.textilflow.platform.profiles.infrastructure.cloudinary;

import com.textilflow.platform.profiles.domain.services.ProfileImageService;
import com.textilflow.platform.shared.application.services.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Profile-specific Cloudinary service that adapts shared CloudinaryService
 */
@Service
public class ProfileCloudinaryService implements ProfileImageService {

    private final CloudinaryService cloudinaryService;
    private static final String PROFILES_FOLDER = "textilflow/profiles";

    public ProfileCloudinaryService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public String uploadImage(Long userId, MultipartFile file, String userType) {
        String folder = PROFILES_FOLDER + "/" + userType.toLowerCase();
        String publicId = generatePublicId(userId, userType);

        // ✅ USAR EL MÉTODO SOBRECARGADO:
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

    private String generatePublicId(Long userId, String userType) {
        return String.format("%s_%d_logo", userType.toLowerCase(), userId);
    }
}