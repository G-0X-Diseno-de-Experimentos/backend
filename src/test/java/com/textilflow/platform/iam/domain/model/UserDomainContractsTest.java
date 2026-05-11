package com.textilflow.platform.iam.domain.model;

import com.textilflow.platform.iam.domain.model.aggregates.User;
import com.textilflow.platform.iam.domain.model.valueobjects.Roles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDomainContractsTest {

    @Test
    @DisplayName("Constructor vacío debe inicializar rol PENDING (AAA)")
    void emptyConstructor_ShouldInitializePendingRole() {
        // Arrange & Act
        User user = new User();

        // Assert
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getCountry());
        assertNull(user.getCity());
        assertNull(user.getAddress());
        assertNull(user.getPhone());
        assertEquals(Roles.PENDING, user.getRole());
    }

    @Test
    @DisplayName("Constructor básico debe asignar valores correctamente (AAA)")
    void basicConstructor_ShouldAssignValues() {
        // Arrange & Act
        User user = new User(
                "John Doe",
                "john@mail.com",
                "123456",
                "Peru",
                "Lima",
                "Av. Test 123",
                "999999999"
        );

        // Assert
        assertEquals("John Doe", user.getName());
        assertEquals("john@mail.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertEquals("Peru", user.getCountry());
        assertEquals("Lima", user.getCity());
        assertEquals("Av. Test 123", user.getAddress());
        assertEquals("999999999", user.getPhone());
        assertEquals(Roles.PENDING, user.getRole());
    }

    @Test
    @DisplayName("Constructor con rol debe asignarlo correctamente (AAA)")
    void constructorWithRole_ShouldAssignRole() {
        // Arrange & Act
        User user = new User(
                "Ana",
                "ana@mail.com",
                "pass",
                "Peru",
                "Lima",
                "Direccion",
                "111111111",
                Roles.SUPPLIER
        );

        // Assert
        assertEquals(Roles.SUPPLIER, user.getRole());
    }

    @Test
    @DisplayName("updateRole debe cambiar el rol correctamente (AAA)")
    void updateRole_ShouldChangeRole() {
        // Arrange
        User user = new User(
                "Carlos",
                "carlos@mail.com",
                "pass",
                "Peru",
                "Lima",
                "Dir",
                "999999999"
        );

        assertEquals(Roles.PENDING, user.getRole());

        // Act
        user.updateRole(Roles.BUSINESSMAN);

        // Assert
        assertEquals(Roles.BUSINESSMAN, user.getRole());
        assertEquals("BUSINESSMAN", user.getRoleName());
    }

    @Test
    @DisplayName("getRoleName debe retornar nombre del enum (AAA)")
    void getRoleName_ShouldReturnEnumName() {
        // Arrange
        User user = new User(
                "Luis",
                "luis@mail.com",
                "pass",
                "Peru",
                "Lima",
                "Dir",
                "999999999",
                Roles.SUPPLIER
        );

        // Act & Assert
        assertEquals("SUPPLIER", user.getRoleName());
    }

    @Test
    @DisplayName("Constructor debe manejar nulls en campos obligatorios (edge case)")
    void constructor_ShouldHandleNullValues() {
        // Arrange & Act
        User user = new User(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Assert
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getCountry());
        assertNull(user.getCity());
        assertNull(user.getAddress());
        assertNull(user.getPhone());
        assertEquals(Roles.PENDING, user.getRole());
    }

    @Test
    @DisplayName("updateRole debe manejar null (edge case)")
    void updateRole_ShouldHandleNull() {
        // Arrange
        User user = new User(
                "Carlos",
                "c@mail.com",
                "pass",
                "Peru",
                "Lima",
                "Dir",
                "999"
        );

        // Act
        user.updateRole(null);

        // Assert
        assertNull(user.getRole());
    }

    @Test
    @DisplayName("email vacío o null debe ser aceptado por aggregate (sin validación fuerte)")
    void email_ShouldAllowNullOrEmpty() {
        // Arrange & Act
        User user = new User(
                "Name",
                "",
                "",
                "Peru",
                "Lima",
                "Dir",
                "999"
        );

        // Assert
        assertEquals("", user.getEmail());
        assertEquals("", user.getPassword());
    }

    @Test
    @DisplayName("getRoleName debe fallar o manejar role null")
    void getRoleName_ShouldHandleNullRole() {
        // Arrange
        User user = new User();
        user.setRole(null);

        // Act & Assert
        assertThrows(NullPointerException.class, user::getRoleName);
    }
}