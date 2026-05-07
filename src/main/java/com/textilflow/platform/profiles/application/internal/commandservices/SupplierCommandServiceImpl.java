package com.textilflow.platform.profiles.application.internal.commandservices;

import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import com.textilflow.platform.profiles.domain.model.commands.*;
import com.textilflow.platform.profiles.domain.model.events.SupplierCreatedEvent;
import com.textilflow.platform.profiles.domain.model.events.LogoUploadedEvent;
import com.textilflow.platform.profiles.domain.model.valueobjects.*;
import com.textilflow.platform.profiles.domain.services.SupplierCommandService;
import com.textilflow.platform.profiles.domain.services.ProfileImageService;
import com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories.SupplierRepository;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Supplier command service implementation
 */
@Service
public class SupplierCommandServiceImpl implements SupplierCommandService {

    private final SupplierRepository supplierRepository;
    private final ProfileImageService profileImageService;
    private final ExternalIamService externalIamService;
    private final ApplicationEventPublisher eventPublisher;

    public SupplierCommandServiceImpl(SupplierRepository supplierRepository,
                                      ProfileImageService profileImageService,
                                      ExternalIamService externalIamService,
                                      ApplicationEventPublisher eventPublisher) {
        this.supplierRepository = supplierRepository;
        this.profileImageService = profileImageService;
        this.externalIamService = externalIamService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Optional<Supplier> handle(CreateSupplierCommand command) {
        // Verify user exists in IAM
        if (!externalIamService.userExists(command.userId())) {
            throw new RuntimeException("User not found in IAM context");
        }

        // Check if supplier already exists
        if (supplierRepository.existsByUserId(command.userId())) {
            throw new RuntimeException("Supplier profile already exists for this user");
        }

        // ✅ USAR CONSTRUCTOR QUE ACEPTA STRINGS (maneja null internamente)
        var supplier = new Supplier(
                command.userId(),
                command.companyName(),     // String - puede ser null
                command.ruc(),             // String - puede ser null
                command.specialization(),  // String - puede ser null
                command.description(),     // String - puede ser null
                command.certifications()   // String - puede ser null
        );

        var savedSupplier = supplierRepository.save(supplier);

        // Publish domain event (solo si no es null)
        var event = new SupplierCreatedEvent(
                savedSupplier.getUserId(),
                savedSupplier.getCompanyNameValue(),
                savedSupplier.getRucValue(),
                savedSupplier.getSpecializationValue()
        );
        eventPublisher.publishEvent(event);

        return Optional.of(savedSupplier);
    }

    @Override
    public Optional<Supplier> handle(UpdateSupplierCommand command) {
        var supplier = supplierRepository.findByUserId(command.userId());
        if (supplier.isEmpty()) {
            throw new RuntimeException("Supplier not found");
        }

        // ✅ CREAR VALUE OBJECTS SOLO SI NO SON NULL
        CompanyName companyName = command.companyName() != null ? new CompanyName(command.companyName()) : null;
        Ruc ruc = command.ruc() != null ? new Ruc(command.ruc()) : null;
        Specialization specialization = command.specialization() != null ? new Specialization(command.specialization()) : null;

        // Update supplier information
        supplier.get().updateInformation(
                companyName,
                ruc,
                specialization,
                command.description(),
                command.certifications()
        );

        // Update user data in IAM context
        externalIamService.updateUserData(
                command.userId(),
                command.name(),
                command.email(),
                command.country(),
                command.city(),
                command.address(),
                command.phone()
        );

        var updatedSupplier = supplierRepository.save(supplier.get());
        return Optional.of(updatedSupplier);
    }

    @Override
    public Optional<Supplier> handle(UploadLogoCommand command) {
        var supplier = supplierRepository.findByUserId(command.userId());
        if (supplier.isEmpty()) {
            throw new RuntimeException("Supplier not found");
        }

        // Delete old logo if exists
        if (supplier.get().getLogoUrlValue() != null) {
            profileImageService.deleteImage(supplier.get().getLogoUrlValue());
        }

        // Upload new logo
        String logoUrl = profileImageService.uploadImage(
                command.userId(),
                command.logoFile(),
                "SUPPLIER"
        );

        // Update supplier with new logo URL
        supplier.get().updateLogo(new LogoUrl(logoUrl));
        var updatedSupplier = supplierRepository.save(supplier.get());

        // Publish logo uploaded event
        var event = new LogoUploadedEvent(
                command.userId(),
                logoUrl,
                "SUPPLIER"
        );
        eventPublisher.publishEvent(event);

        return Optional.of(updatedSupplier);
    }

    @Override
    public Optional<Supplier> handle(DeleteLogoCommand command) {
        var supplier = supplierRepository.findByUserId(command.userId());
        if (supplier.isEmpty()) {
            throw new RuntimeException("Supplier not found");
        }

        // Delete logo from Cloudinary
        if (supplier.get().getLogoUrlValue() != null) {
            profileImageService.deleteImage(supplier.get().getLogoUrlValue());
        }

        // Update supplier with empty logo URL
        supplier.get().updateLogo(new LogoUrl());
        var updatedSupplier = supplierRepository.save(supplier.get());

        return Optional.of(updatedSupplier);
    }
}
