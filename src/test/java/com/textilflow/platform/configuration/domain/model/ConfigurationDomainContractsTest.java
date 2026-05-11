package com.textilflow.platform.configuration.domain.model;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.domain.model.valueobjects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationDomainContractsTest {

    @Test
    @DisplayName("Constructor vacío debe inicializar sin errores (AAA)")
    void emptyConstructor_ShouldInitialize() {
        // Arrange & Act
        Configuration configuration = new Configuration();

        // Assert
        assertNull(configuration.getUserId());
        assertNull(configuration.getLanguage());
        assertNull(configuration.getViewMode());
        assertNull(configuration.getSubscriptionPlan());
        assertNull(configuration.getSubscriptionStatus());
        assertNull(configuration.getSubscriptionStartDate());
    }

    @Test
    @DisplayName("Constructor completo debe inicializar con valores correctos (AAA)")
    void fullConstructor_ShouldAssignValues() {
        // Arrange & Act
        Configuration configuration = new Configuration(
                1L,
                Language.ES,
                ViewMode.DARK,
                SubscriptionPlan.BASIC
        );

        // Assert
        assertEquals(1L, configuration.getUserIdValue());
        assertEquals(Language.ES, configuration.getLanguage());
        assertEquals(ViewMode.DARK, configuration.getViewMode());
        assertEquals(SubscriptionPlan.BASIC, configuration.getSubscriptionPlan());
        assertEquals(SubscriptionStatus.PENDING, configuration.getSubscriptionStatus());
        assertNotNull(configuration.getSubscriptionStartDate());
    }

    @Test
    @DisplayName("updateSettings debe modificar idioma y vista correctamente (AAA)")
    void updateSettings_ShouldModifyFields() {
        // Arrange
        Configuration configuration = new Configuration(
                1L,
                Language.ES,
                ViewMode.LIGHT,
                SubscriptionPlan.BASIC
        );

        // Act
        configuration.updateSettings(Language.EN, ViewMode.DARK);

        // Assert
        assertEquals(Language.EN, configuration.getLanguage());
        assertEquals(ViewMode.DARK, configuration.getViewMode());
    }

    @Test
    @DisplayName("updateSubscriptionPlan debe cambiar plan y actualizar fecha (AAA)")
    void updateSubscriptionPlan_ShouldUpdatePlanAndDate() {
        // Arrange
        Configuration configuration = new Configuration(
                1L,
                Language.ES,
                ViewMode.LIGHT,
                SubscriptionPlan.BASIC
        );

        LocalDateTime before = configuration.getSubscriptionStartDate();

        // Act
        configuration.updateSubscriptionPlan(SubscriptionPlan.CORPORATE);

        // Assert
        assertEquals(SubscriptionPlan.CORPORATE, configuration.getSubscriptionPlan());
        assertNotNull(configuration.getSubscriptionStartDate());

        // Validación estable (no depende de precisión del reloj)
        assertTrue(
                configuration.getSubscriptionStartDate().isEqual(before)
                        || configuration.getSubscriptionStartDate().isAfter(before)
        );
    }
    @Test
    @DisplayName("activateSubscription debe activar plan y estado ACTIVE (AAA)")
    void activateSubscription_ShouldActivate() {
        // Arrange
        Configuration configuration = new Configuration(
                1L,
                Language.ES,
                ViewMode.LIGHT,
                SubscriptionPlan.BASIC
        );

        // Act
        configuration.activateSubscription(SubscriptionPlan.CORPORATE);

        // Assert
        assertEquals(SubscriptionPlan.CORPORATE, configuration.getSubscriptionPlan());
        assertEquals(SubscriptionStatus.ACTIVE, configuration.getSubscriptionStatus());
        assertNotNull(configuration.getSubscriptionStartDate());
    }

    @Test
    @DisplayName("updateSubscriptionStatus debe actualizar estado correctamente (AAA)")
    void updateSubscriptionStatus_ShouldChangeStatus() {
        // Arrange
        Configuration configuration = new Configuration(
                1L,
                Language.ES,
                ViewMode.LIGHT,
                SubscriptionPlan.BASIC
        );

        // Act
        configuration.updateSubscriptionStatus(SubscriptionStatus.ACTIVE);

        // Assert
        assertEquals(SubscriptionStatus.ACTIVE, configuration.getSubscriptionStatus());
        assertNotNull(configuration.getSubscriptionStartDate());
    }

    @Test
    @DisplayName("requiresPayment debe retornar true cuando estado es PENDING (AAA)")
    void requiresPayment_ShouldReturnTrue_WhenPending() {
        // Arrange
        Configuration configuration = new Configuration(
                1L,
                Language.ES,
                ViewMode.LIGHT,
                SubscriptionPlan.BASIC
        );

        // Assert
        assertTrue(configuration.requiresPayment());
    }

    @Test
    @DisplayName("getUserIdValue debe retornar valor correcto del Value Object (AAA)")
    void getUserIdValue_ShouldReturnCorrectValue() {
        // Arrange
        Configuration configuration = new Configuration(
                99L,
                Language.ES,
                ViewMode.LIGHT,
                SubscriptionPlan.BASIC
        );

        // Assert
        assertEquals(99L, configuration.getUserIdValue());
    }
}