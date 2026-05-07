package com.textilflow.platform.profiles.domain.model.aggregates;

import com.textilflow.platform.profiles.domain.model.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Businessman aggregate root
 * ✅ NO hereda de AuditableAbstractAggregateRoot para evitar conflicto de IDs
 */
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class) // ✅ Para auditoría manual
@Table(name = "businessmen")
public class Businessman {

    @Id
    private Long userId; // ✅ ÚNICO @Id - mismo que User ID del IAM

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "company_name"))
    })
    private CompanyName companyName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "number", column = @Column(name = "ruc"))
    })
    private Ruc ruc;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "business_type"))
    })
    private BusinessType businessType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "logo_url"))
    })
    private LogoUrl logoUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String website;

    // ✅ CAMPOS DE AUDITORÍA MANUALES:
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;

    public Businessman() {
        // Required by JPA
    }

    public Businessman(Long userId) {
        this.userId = userId;
        this.logoUrl = new LogoUrl();
    }

    public Businessman(Long userId, String companyName, String ruc,
                       String businessType, String description, String website) {
        this.userId = userId;
        this.companyName = companyName != null ? new CompanyName(companyName) : null;
        this.ruc = ruc != null ? new Ruc(ruc) : null;
        this.businessType = businessType != null ? new BusinessType(businessType) : null;
        this.description = description;
        this.website = website;
        this.logoUrl = new LogoUrl();
    }

    /**
     * Update businessman information
     */
    public void updateInformation(CompanyName companyName, Ruc ruc,
                                  BusinessType businessType, String description, String website) {
        this.companyName = companyName;
        this.ruc = ruc;
        this.businessType = businessType;
        this.description = description;
        this.website = website;
    }

    /**
     * Update logo URL
     */
    public void updateLogo(LogoUrl logoUrl) {
        this.logoUrl = logoUrl;
    }

    /**
     * Get company name as string
     */
    public String getCompanyNameValue() {
        return companyName != null ? companyName.name() : null;
    }

    /**
     * Get RUC as string
     */
    public String getRucValue() {
        return ruc != null ? ruc.number() : null;
    }

    /**
     * Get business type as string
     */
    public String getBusinessTypeValue() {
        return businessType != null ? businessType.type() : null;
    }

    /**
     * Get logo URL as string
     */
    public String getLogoUrlValue() {
        return logoUrl != null ? logoUrl.url() : null;
    }
}