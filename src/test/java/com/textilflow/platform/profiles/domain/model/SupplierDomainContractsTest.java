package com.textilflow.platform.profiles.domain.model;

import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import com.textilflow.platform.profiles.domain.model.valueobjects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierDomainContractsTest {

    @Test
    @DisplayName("Constructor vacío debe inicializar sin valores (AAA)")
    void emptyConstructor_ShouldInitialize() {
        // Arrange & Act
        Supplier supplier = new Supplier();

        // Assert
        assertNull(supplier.getUserId());
        assertNull(supplier.getCompanyName());
        assertNull(supplier.getRuc());
        assertNull(supplier.getSpecialization());
        assertNull(supplier.getDescription());
        assertNull(supplier.getCertifications());
        assertNull(supplier.getLogoUrlValue());
    }

    @Test
    @DisplayName("Constructor con userId debe inicializar logo vacío (AAA)")
    void constructorWithUserId_ShouldInitializeLogo() {
        // Arrange & Act
        Supplier supplier = new Supplier(1L);

        // Assert
        assertEquals(1L, supplier.getUserId());
        assertNull(supplier.getLogoUrlValue());
    }

    @Test
    @DisplayName("Constructor completo debe asignar valores correctamente (AAA)")
    void fullConstructor_ShouldAssignValues() {
        // Act
        Supplier supplier = new Supplier(
                1L,
                "Supplier SAC",
                "12345678901",
                "TEXTILE",
                "Proveedor textil",
                "ISO9001"
        );

        // Assert
        assertEquals("Supplier SAC", supplier.getCompanyNameValue());
        assertEquals("12345678901", supplier.getRucValue());
        assertEquals("TEXTILE", supplier.getSpecializationValue());
        assertEquals("Proveedor textil", supplier.getDescription());
        assertEquals("ISO9001", supplier.getCertifications());
    }

    @Test
    @DisplayName("updateInformation debe actualizar campos correctamente (AAA)")
    void updateInformation_ShouldUpdateFields() {
        // Arrange
        Supplier supplier = new Supplier(1L);

        // Act
        supplier.updateInformation(
                new CompanyName("Nueva Supplier"),
                new Ruc("12345678901"),
                new Specialization("INDUSTRIAL"),
                "desc actualizada",
                "ISO14001"
        );

        // Assert
        assertEquals("Nueva Supplier", supplier.getCompanyNameValue());
        assertEquals("12345678901", supplier.getRucValue());
        assertEquals("INDUSTRIAL", supplier.getSpecializationValue());
        assertEquals("desc actualizada", supplier.getDescription());
        assertEquals("ISO14001", supplier.getCertifications());
    }

    @Test
    @DisplayName("updateLogo debe actualizar correctamente (AAA)")
    void updateLogo_ShouldChangeLogo() {
        // Arrange
        Supplier supplier = new Supplier(1L);

        // Act
        supplier.updateLogo(new LogoUrl("https://logo.com/supplier.png"));

        // Assert
        assertEquals("https://logo.com/supplier.png", supplier.getLogoUrlValue());
    }
}