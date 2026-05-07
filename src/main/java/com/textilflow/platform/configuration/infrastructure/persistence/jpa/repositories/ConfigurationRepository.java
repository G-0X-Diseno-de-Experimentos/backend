
package com.textilflow.platform.configuration.infrastructure.persistence.jpa.repositories;

import com.textilflow.platform.configuration.domain.model.aggregates.Configuration;
import com.textilflow.platform.configuration.domain.model.valueobjects.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Configuration repository interface
 * Provides data access methods for Configuration aggregate
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    /**
     * Find configuration by user ID
     * Uses embedded value object path
     */
    Optional<Configuration> findByUserId_Value(Long userId);

    /**
     * Check if configuration exists for user ID
     */
    boolean existsByUserId_Value(Long userId);



    /**
     * Find all configurations by subscription plan
     */
    List<Configuration> findBySubscriptionPlan(SubscriptionPlan subscriptionPlan);

    /**
     * Find configurations with expired corporate subscriptions
     * (Corporate subscriptions older than 30 days)
     */
    @Query("SELECT c FROM Configuration c WHERE c.subscriptionPlan = :subscriptionPlan " +
            "AND c.subscriptionStartDate < :expirationDate")
    List<Configuration> findExpiredSubscriptions(
            @Param("subscriptionPlan") SubscriptionPlan subscriptionPlan,
            @Param("expirationDate") LocalDateTime expirationDate
    );

    /**
     * Find configurations by language
     */
    @Query("SELECT c FROM Configuration c WHERE c.language = :language")
    List<Configuration> findByLanguage(@Param("language") String language);

    /**
     * Find configurations by view mode
     */
    @Query("SELECT c FROM Configuration c WHERE c.viewMode = :viewMode")
    List<Configuration> findByViewMode(@Param("viewMode") String viewMode);

    /**
     * Count configurations by subscription plan
     */
    long countBySubscriptionPlan(SubscriptionPlan subscriptionPlan);

    /**
     * Find configurations created after specific date
     */
    @Query("SELECT c FROM Configuration c WHERE c.createdAt >= :date")
    List<Configuration> findCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find configurations updated after specific date
     */
    @Query("SELECT c FROM Configuration c WHERE c.updatedAt >= :date")
    List<Configuration> findUpdatedAfter(@Param("date") LocalDateTime date);

    /**
     * Batch update expired corporate subscriptions to basic
     * Custom update query for performance
     */
    @Query("UPDATE Configuration c SET c.subscriptionPlan = :newPlan " +
            "WHERE c.subscriptionPlan = :currentPlan " +
            "AND c.subscriptionStartDate < :expirationDate")
    int updateExpiredSubscriptionsToBasic(
            @Param("currentPlan") SubscriptionPlan currentPlan,
            @Param("newPlan") SubscriptionPlan newPlan,
            @Param("expirationDate") LocalDateTime expirationDate
    );



    /**
     * Count active corporate subscriptions
     * (Corporate subscriptions within 30 days)
     */
    @Query("SELECT COUNT(c) FROM Configuration c WHERE c.subscriptionPlan = :subscriptionPlan " +
            "AND c.subscriptionStartDate >= :validDate")
    long countActiveCorporateSubscriptions(
            @Param("subscriptionPlan") SubscriptionPlan subscriptionPlan,
            @Param("validDate") LocalDateTime validDate
    );

    /**
     * Find user configurations by multiple criteria
     * Advanced search method
     */
    @Query("SELECT c FROM Configuration c WHERE " +

            "(:language IS NULL OR c.language = :language) AND " +
            "(:viewMode IS NULL OR c.viewMode = :viewMode) AND " +
            "(:subscriptionPlan IS NULL OR c.subscriptionPlan = :subscriptionPlan)")
    List<Configuration> findByCriteria(
            @Param("language") String language,
            @Param("viewMode") String viewMode,
            @Param("subscriptionPlan") SubscriptionPlan subscriptionPlan
    );

    /**
     * Delete configuration by user ID
     * Useful for user account deletion scenarios
     */
    void deleteByUserId_Value(Long userId);

    /**
     * Find configurations that need subscription check
     * (Corporate subscriptions that might be expired)
     */
    @Query("SELECT c FROM Configuration c WHERE c.subscriptionPlan = 'CORPORATE' " +
            "AND c.subscriptionStartDate IS NOT NULL")
    List<Configuration> findCorporateSubscriptionsForCheck();
}