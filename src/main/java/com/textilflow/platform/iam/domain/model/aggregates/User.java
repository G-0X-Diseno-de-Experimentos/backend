package com.textilflow.platform.iam.domain.model.aggregates;

import com.textilflow.platform.iam.domain.model.valueobjects.Roles;
import com.textilflow.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * User aggregate root for IAM context
 * This class represents the user entity for authentication and authorization
 */
@Getter
@Setter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 255)
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Roles role;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String country;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String city;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String address;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String phone;

    public User() {
        this.role = Roles.PENDING;
    }

    public User(String name, String email, String password, String country,
                String city, String address, String phone) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
        this.country = country;
        this.city = city;
        this.address = address;
        this.phone = phone;
    }

    public User(String name, String email, String password, String country,
                String city, String address, String phone, Roles role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.country = country;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.role = role;
    }

    /**
     * Update user role
     * @param newRole the new role to assign
     */
    public void updateRole(Roles newRole) {
        Roles oldRole = this.role;
        this.role = newRole;
        // Register domain event for role update
        // This will be handled by event handlers to notify other contexts
    }

    /**
     * Get role as string
     * @return role as string
     */
    public String getRoleName() {
        return this.role.name();
    }
}