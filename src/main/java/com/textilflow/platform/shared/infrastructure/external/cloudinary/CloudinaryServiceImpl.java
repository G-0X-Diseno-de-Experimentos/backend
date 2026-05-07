package com.textilflow.platform.shared.infrastructure.external.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.textilflow.platform.shared.application.services.CloudinaryService; // ✅ AGREGAR IMPORT
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        try {
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "image",
                    "quality", "auto:good",
                    "fetch_format", "auto"
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary: " + e.getMessage(), e);
        }
    }

    // ✅ IMPLEMENTAR MÉTODO SOBRECARGADO:
    @Override
    public String uploadImage(MultipartFile file, String folder, String publicId) {
        try {
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "folder", folder,
                    "public_id", publicId,
                    "resource_type", "image",
                    "quality", "auto:good",
                    "fetch_format", "auto"
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteImage(String publicId) {
        try {
            Map<?, ?> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(deleteResult.get("result"));
        } catch (IOException e) {
            throw new RuntimeException("Error deleting image from Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public String extractPublicId(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        // Extract public ID from Cloudinary URL
        String[] parts = imageUrl.split("/");
        for (int i = 0; i < parts.length; i++) {
            if ("upload".equals(parts[i]) && i + 2 < parts.length) {
                // Skip version if present (v1234567890)
                int startIndex = i + 1;
                if (parts[startIndex].startsWith("v") && parts[startIndex].length() > 1) {
                    startIndex = i + 2;
                }

                StringBuilder publicId = new StringBuilder();
                for (int j = startIndex; j < parts.length; j++) {
                    if (j > startIndex) publicId.append("/");
                    String part = parts[j];
                    // Remove file extension from last part
                    if (j == parts.length - 1) {
                        int dotIndex = part.lastIndexOf('.');
                        part = dotIndex > 0 ? part.substring(0, dotIndex) : part;
                    }
                    publicId.append(part);
                }
                return publicId.toString();
            }
        }
        return null;
    }
}