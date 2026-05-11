package com.textilflow.platform.profiles.domain.model;

import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import com.textilflow.platform.profiles.domain.model.valueobjects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessmanDomainContractsTest {

    @Test
    @DisplayName("Constructor vacío debe inicializar logo vacío (AAA)")
    void emptyConstructor_ShouldInitialize() {
        // Arrange & Act
        Businessman businessman = new Businessman();

        // Assert
        assertNull(businessman.getUserId());
        assertNull(businessman.getCompanyName());
        assertNull(businessman.getRuc());
        assertNull(businessman.getBusinessType());
        assertNull(businessman.getDescription());
        assertNull(businessman.getWebsite());
        assertNull(businessman.getLogoUrlValue());
    }

    @Test
    @DisplayName("Constructor con userId debe inicializar logo vacío (AAA)")
    void constructorWithUserId_ShouldInitializeLogo() {
        // Arrange & Act
        Businessman businessman = new Businessman(1L);

        // Assert
        assertEquals(1L, businessman.getUserId());
        assertTrue(businessman.getLogoUrlValue() == null || businessman.getLogoUrlValue().isBlank());
    }

    @Test
    @DisplayName("Constructor completo debe asignar valores correctamente (AAA)")
    void fullConstructor_ShouldAssignValues() {
        // Act
        Businessman businessman = new Businessman(
                1L,
                "Textil SAC",
                "12345678901",
                "INDUSTRIAL",
                "Empresa textil",
                "https://textil.com"
        );

        // Assert
        assertEquals("Textil SAC", businessman.getCompanyNameValue());
        assertEquals("12345678901", businessman.getRucValue());
        assertEquals("INDUSTRIAL", businessman.getBusinessTypeValue());
        assertEquals("Empresa textil", businessman.getDescription());
        assertEquals("https://textil.com", businessman.getWebsite());
    }

    @Test
    @DisplayName("updateInformation debe actualizar todos los campos (AAA)")
    void updateInformation_ShouldUpdateFields() {
        // Arrange
        Businessman businessman = new Businessman(1L);

        // Act
        businessman.updateInformation(
                new CompanyName("Nueva Empresa"),
                new Ruc("12345678901"),
                new BusinessType("SERVICE"),
                "Nueva descripción",
                "https://new.com"
        );

        // Assert
        assertEquals("Nueva Empresa", businessman.getCompanyNameValue());
        assertEquals("12345678901", businessman.getRucValue());
        assertEquals("SERVICE", businessman.getBusinessTypeValue());
        assertEquals("Nueva descripción", businessman.getDescription());
        assertEquals("https://new.com", businessman.getWebsite());
    }

    @Test
    @DisplayName("updateLogo debe cambiar URL correctamente (AAA)")
    void updateLogo_ShouldChangeLogo() {
        // Arrange
        Businessman businessman = new Businessman(1L);

        // Act
        businessman.updateLogo(new LogoUrl("https://logo.com/img.png"));

        // Assert
        assertEquals("https://logo.com/img.png", businessman.getLogoUrlValue());
    }
}