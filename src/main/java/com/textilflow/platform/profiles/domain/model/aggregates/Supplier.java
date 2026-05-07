package com.textilflow.platform.profiles.domain.model.aggregates;

import com.textilflow.platform.profiles.domain.model.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Supplier aggregate root
 * ✅ NO hereda de AuditableAbstractAggregateRoot para evitar conflicto de IDs
 */
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class) // ✅ Para auditoría manual
@Table(name = "suppliers")
public class Supplier {

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
            @AttributeOverride(name = "area", column = @Column(name = "specialization"))
    })
    private Specialization specialization;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "logo_url"))
    })
    private LogoUrl logoUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String certifications;

    // ✅ CAMPOS DE AUDITORÍA MANUALES:
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;

    public Supplier() {
        // Required by JPA
    }

    public Supplier(Long userId) {
        this.userId = userId;
        this.logoUrl = new LogoUrl();
    }

    public Supplier(Long userId, String companyName, String ruc,
                    String specialization, String description, String certifications) {
        this.userId = userId;
        this.companyName = companyName != null ? new CompanyName(companyName) : null;
        this.ruc = ruc != null ? new Ruc(ruc) : null;
        this.specialization = specialization != null ? new Specialization(specialization) : null;
        this.description = description;
        this.certifications = certifications;
        this.logoUrl = new LogoUrl();
    }

    /**
     * Update supplier information
     */
    public void updateInformation(CompanyName companyName, Ruc ruc,
                                  Specialization specialization, String description, String certifications) {
        this.companyName = companyName;
        this.ruc = ruc;
        this.specialization = specialization;
        this.description = description;
        this.certifications = certifications;
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
     * Get specialization as string
     */
    public String getSpecializationValue() {
        return specialization != null ? specialization.area() : null;
    }

    /**
     * Get logo URL as string
     */
    public String getLogoUrlValue() {
        return logoUrl != null ? logoUrl.url() : null;
    }
}