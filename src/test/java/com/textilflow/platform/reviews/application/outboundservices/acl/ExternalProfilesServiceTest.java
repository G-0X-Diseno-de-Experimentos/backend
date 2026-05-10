package com.textilflow.platform.reviews.application.outboundservices.acl;

import com.textilflow.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalProfilesServiceTest {

    @Mock
    private ProfilesContextFacade profilesContextFacade;

    @InjectMocks
    private ExternalProfilesService service;

    @Test
    @DisplayName("getBusinessmanProfileId debe retornar Optional del ID si existe (AAA)")
    void getBusinessmanProfileId_ShouldReturnId_WhenExists() {
        // Arrange
        when(profilesContextFacade.hasBusinessmanProfile(100L)).thenReturn(true);
        when(profilesContextFacade.getBusinessmanByUserId(100L)).thenReturn(10L);

        // Act
        Optional<Long> result = service.getBusinessmanProfileId(100L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(10L, result.get());
    }

    @Test
    @DisplayName("getBusinessmanProfileId debe retornar Optional vacío si lanza excepción (AAA)")
    void getBusinessmanProfileId_ShouldReturnEmpty_OnError() {
        // Arrange
        when(profilesContextFacade.hasBusinessmanProfile(100L)).thenThrow(new RuntimeException("ACL Error"));

        // Act
        Optional<Long> result = service.getBusinessmanProfileId(100L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getSupplierProfileId debe retornar Optional del ID si existe (AAA)")
    void getSupplierProfileId_ShouldReturnId_WhenExists() {
        // Arrange
        when(profilesContextFacade.hasSupplierProfile(200L)).thenReturn(true);
        when(profilesContextFacade.getSupplierByUserId(200L)).thenReturn(20L);

        // Act
        Optional<Long> result = service.getSupplierProfileId(200L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(20L, result.get());
    }

    @Test
    @DisplayName("hasBusinessmanProfile y hasSupplierProfile deben manejar excepciones y retornar false (AAA)")
    void hasProfiles_ShouldHandleExceptions() {
        // Arrange
        when(profilesContextFacade.hasBusinessmanProfile(99L)).thenThrow(new RuntimeException("Error"));
        when(profilesContextFacade.hasSupplierProfile(99L)).thenThrow(new RuntimeException("Error"));

        // Act & Assert
        assertFalse(service.hasBusinessmanProfile(99L));
        assertFalse(service.hasSupplierProfile(99L));
    }

    @Test
    @DisplayName("getCompanyNameByUserId debe retornar el nombre si es válido (AAA)")
    void getCompanyNameByUserId_ShouldReturnName() {
        // Arrange
        when(profilesContextFacade.getCompanyNameByUserId(50L)).thenReturn("Textiles S.A.");

        // Act
        Optional<String> result = service.getCompanyNameByUserId(50L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Textiles S.A.", result.get());
    }

    @Test
    @DisplayName("Validaciones de ID directo deben responder correctamente (AAA)")
    void idValidations_ShouldRespondCorrectly() {
        // Act & Assert
        assertTrue(service.isValidBusinessmanId(1L));
        assertFalse(service.isValidBusinessmanId(0L));
        assertFalse(service.isValidBusinessmanId(null));

        assertTrue(service.isValidSupplierId(1L));
        assertFalse(service.isValidSupplierId(0L));
        assertFalse(service.isValidSupplierId(null));
    }
}