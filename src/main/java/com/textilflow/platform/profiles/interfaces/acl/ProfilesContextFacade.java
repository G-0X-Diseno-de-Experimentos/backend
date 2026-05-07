package com.textilflow.platform.profiles.interfaces.acl;

/**
 * Profiles context facade for external communication
 */
public interface ProfilesContextFacade {

    /**
     * Get businessman profile by user ID
     */
    Long getBusinessmanByUserId(Long userId);

    /**
     * Get supplier profile by user ID
     */
    Long getSupplierByUserId(Long userId);

    /**
     * Check if user has businessman profile
     */
    boolean hasBusinessmanProfile(Long userId);

    /**
     * Check if user has supplier profile
     */
    boolean hasSupplierProfile(Long userId);

    /**
     * Get company name by user ID (works for both businessman and supplier)
     */
    String getCompanyNameByUserId(Long userId);
}