package com.textilflow.platform.profiles.domain.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Profile image service interface
 */
public interface ProfileImageService {

    /**
     * Upload profile image
     */
    String uploadImage(Long userId, MultipartFile file, String userType);

    /**
     * Delete profile image
     */
    void deleteImage(String imageUrl);
}