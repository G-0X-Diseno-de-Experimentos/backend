package com.textilflow.platform.shared.application.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Cloudinary Service
 * Interface for managing image uploads to Cloudinary
 */
public interface CloudinaryService {

    /**
     * Upload image to Cloudinary
     * @param file The image file to upload
     * @param folder The folder in Cloudinary to store the image
     * @return The URL of the uploaded image
     */
    String uploadImage(MultipartFile file, String folder);

    /**
     * Delete image from Cloudinary
     * @param publicId The public ID of the image to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteImage(String publicId);

    /**
     * Extract public ID from Cloudinary URL
     * @param imageUrl The Cloudinary image URL
     * @return The public ID of the image
     */
    String extractPublicId(String imageUrl);

    // ✅ AGREGAR MÉTODO SOBRECARGADO para mayor flexibilidad:
    /**
     * Upload image with custom public ID
     * @param file The image file to upload
     * @param folder The folder in Cloudinary to store the image
     * @param publicId Custom public ID for the image
     * @return The URL of the uploaded image
     */
    default String uploadImage(MultipartFile file, String folder, String publicId) {
        // Por defecto usa el método básico, implementación puede sobrescribir
        return uploadImage(file, folder);
    }
}