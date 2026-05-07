package com.textilflow.platform.configuration.domain.model.aggregates;

import com.textilflow.platform.configuration.domain.model.valueobjects.*;
import com.textilflow.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

/**
 * Configuration aggregate root
 * Manages user preferences and subscription settings
 */
@Getter
@Entity
@Table(name = "configurations")
public class Configuration extends AuditableAbstractAggregateRoot<Configuration> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false, unique = true))
    })
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "view_mode", nullable = false)
    private ViewMode viewMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan")
    private SubscriptionPlan subscriptionPlan;

    // *** NUEVO CAMPO ***
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status", nullable = false)
    private SubscriptionStatus subscriptionStatus;

    @Column(name = "subscription_start_date")
    private java.time.LocalDateTime subscriptionStartDate;

    public Configuration() {
        // Required by JPA
    }

    public Configuration(Long userId, Language language, ViewMode viewMode, SubscriptionPlan subscriptionPlan) {
        this.userId = new UserId(userId);
        this.language = language;
        this.viewMode = viewMode;
        this.subscriptionPlan = subscriptionPlan;
        this.subscriptionStatus = SubscriptionStatus.PENDING; // *** NUEVO: Siempre inicia en PENDING ***
        this.subscriptionStartDate = java.time.LocalDateTime.now();
    }

    /**
     * Update configuration settings
     */
    public void updateSettings(Language language, ViewMode viewMode) {
        this.language = language;
        this.viewMode = viewMode;
    }

    /**
     * Update subscription plan
     */
    public void updateSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
        this.subscriptionStartDate = java.time.LocalDateTime.now();
    }

    /**
     * *** NUEVO MÉTODO: Activar suscripción después del pago ***
     */
    public void activateSubscription(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
        this.subscriptionStatus = SubscriptionStatus.ACTIVE;
        this.subscriptionStartDate = java.time.LocalDateTime.now();
    }

    /**
     * *** NUEVO MÉTODO: Cambiar solo el status de suscripción ***
     */
    public void updateSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
        if (subscriptionStatus == SubscriptionStatus.ACTIVE) {
            this.subscriptionStartDate = java.time.LocalDateTime.now();
        }
    }

    /**
     * Check if subscription is expired (more than 30 days for CORPORATE)
     */
    public boolean isSubscriptionExpired() {
        if (subscriptionPlan != SubscriptionPlan.CORPORATE || subscriptionStartDate == null) {
            return false;
        }
        return subscriptionStartDate.plusDays(30).isBefore(java.time.LocalDateTime.now());
    }

    /**
     * Auto-downgrade to basic if subscription expired
     */
    public void checkAndDowngradeSubscription() {
        if (isSubscriptionExpired()) {
            this.subscriptionPlan = SubscriptionPlan.BASIC;
            this.subscriptionStatus = SubscriptionStatus.PENDING; // *** NUEVO: También cambiar status ***
        }
    }

    /**
     * *** NUEVO MÉTODO: Verificar si necesita pago ***
     */
    public boolean requiresPayment() {
        return this.subscriptionStatus == SubscriptionStatus.PENDING;
    }

    /**
     * Get user ID as Long
     */
    public Long getUserIdValue() {
        return userId != null ? userId.value() : null;
    }
}