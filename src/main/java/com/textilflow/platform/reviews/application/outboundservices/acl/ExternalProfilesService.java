package com.textilflow.platform.reviews.application.outboundservices.acl;

import com.textilflow.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ExternalProfilesService
 * Outbound service para comunicación con el contexto Profiles
 * Siguiendo DDD - Anti-Corruption Layer (ACL) para aislamiento de contextos
 */
@Service
public class ExternalProfilesService {

    private final ProfilesContextFacade profilesContextFacade;

    /**
     * Constructor
     * @param profilesContextFacade Facade del contexto Profiles
     */
    public ExternalProfilesService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    /**
     * Obtiene el ID del perfil de businessman por user ID
     * @param userId ID del usuario
     * @return Optional con el ID del businessman si existe
     */
    public Optional<Long> getBusinessmanProfileId(Long userId) {
        try {
            if (profilesContextFacade.hasBusinessmanProfile(userId)) {
                Long businessmanId = profilesContextFacade.getBusinessmanByUserId(userId);
                return businessmanId != null && businessmanId > 0 ? Optional.of(businessmanId) : Optional.empty();
            }
            return Optional.empty();
        } catch (Exception e) {
            // Log error pero no propagar excepción - aislamiento de contextos
            return Optional.empty();
        }
    }

    /**
     * Obtiene el ID del perfil de supplier por user ID
     * @param userId ID del usuario
     * @return Optional con el ID del supplier si existe
     */
    public Optional<Long> getSupplierProfileId(Long userId) {
        try {
            if (profilesContextFacade.hasSupplierProfile(userId)) {
                Long supplierId = profilesContextFacade.getSupplierByUserId(userId);
                return supplierId != null && supplierId > 0 ? Optional.of(supplierId) : Optional.empty();
            }
            return Optional.empty();
        } catch (Exception e) {
            // Log error pero no propagar excepción - aislamiento de contextos
            return Optional.empty();
        }
    }

    /**
     * Verifica si un usuario tiene perfil de businessman
     * @param userId ID del usuario
     * @return true si tiene perfil de businessman, false en caso contrario
     */
    public boolean hasBusinessmanProfile(Long userId) {
        try {
            return profilesContextFacade.hasBusinessmanProfile(userId);
        } catch (Exception e) {
            // En caso de error, asumir que no tiene perfil
            return false;
        }
    }

    /**
     * Verifica si un usuario tiene perfil de supplier
     * @param userId ID del usuario
     * @return true si tiene perfil de supplier, false en caso contrario
     */
    public boolean hasSupplierProfile(Long userId) {
        try {
            return profilesContextFacade.hasSupplierProfile(userId);
        } catch (Exception e) {
            // En caso de error, asumir que no tiene perfil
            return false;
        }
    }

    /**
     * Obtiene el nombre de la empresa por user ID
     * Funciona tanto para businessman como supplier
     * @param userId ID del usuario
     * @return Optional con el nombre de la empresa si existe
     */
    public Optional<String> getCompanyNameByUserId(Long userId) {
        try {
            String companyName = profilesContextFacade.getCompanyNameByUserId(userId);
            return companyName != null && !companyName.isBlank() ? Optional.of(companyName) : Optional.empty();
        } catch (Exception e) {
            // Log error pero no propagar excepción - aislamiento de contextos
            return Optional.empty();
        }
    }

    /**
     * Valida que un businessman ID es válido consultando el contexto profiles
     * @param businessmanId ID del businessman a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean isValidBusinessmanId(Long businessmanId) {
        // Nota: En este caso businessmanId es directo, no necesita conversión de userId
        // Pero podrías agregar validación adicional si es necesario
        return businessmanId != null && businessmanId > 0;
    }

    /**
     * Valida que un supplier ID es válido consultando el contexto profiles
     * @param supplierId ID del supplier a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean isValidSupplierId(Long supplierId) {
        // Nota: En este caso supplierId es directo, no necesita conversión de userId
        // Pero podrías agregar validación adicional si es necesario
        return supplierId != null && supplierId > 0;
    }
}