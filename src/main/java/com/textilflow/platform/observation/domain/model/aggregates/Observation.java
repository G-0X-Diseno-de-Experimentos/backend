package com.textilflow.platform.observation.domain.model.aggregates;

import com.textilflow.platform.observation.domain.model.commands.UpdateObservationCommand;
import com.textilflow.platform.observation.domain.model.valueobjects.BatchCode;
import com.textilflow.platform.observation.domain.model.valueobjects.ImageUrl;
import com.textilflow.platform.observation.domain.model.valueobjects.ObservationStatus;
import com.textilflow.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "observations")
@Getter
public class Observation extends AuditableAbstractAggregateRoot<Observation> {

    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "code", column = @Column(name = "batch_code"))
    })
    private BatchCode batchCode;

    @Column(name = "businessman_id", nullable = false)
    private Long businessmanId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "reason", nullable = false, length = 1000)
    private String reason;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "image_url", columnDefinition = "TEXT"))
    })
    private ImageUrl imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ObservationStatus status;

    public Observation() {}

    public Observation(Long batchId, BatchCode batchCode, Long businessmanId,
                       Long supplierId, String reason, ImageUrl imageUrl,
                       ObservationStatus status) {
        this.batchId = batchId;
        this.batchCode = batchCode;
        this.businessmanId = businessmanId;
        this.supplierId = supplierId;
        this.reason = reason;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public void updateInformation(UpdateObservationCommand command) {
        this.reason = command.reason();
        this.status = ObservationStatus.valueOf(command.status().toUpperCase());
        if (command.imageUrl() != null && !command.imageUrl().isEmpty()) {
            this.imageUrl = new ImageUrl(command.imageUrl());
        }
    }

    // ✅ AGREGAR MÉTODOS PARA MANEJO DE IMÁGENES
    public void updateImage(String imageUrl) {
        this.imageUrl = new ImageUrl(imageUrl);
    }

    public void deleteImage() {
        this.imageUrl = null;
    }

    public String getBatchCodeValue() {
        return this.batchCode != null ? this.batchCode.getCode() : null;
    }

    public String getImageUrlValue() {
        return this.imageUrl != null ? this.imageUrl.getUrl() : null;
    }

    public String getStatusValue() {
        return this.status != null ? this.status.name() : null;
    }
}