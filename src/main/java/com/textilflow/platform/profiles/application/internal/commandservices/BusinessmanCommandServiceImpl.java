package com.textilflow.platform.profiles.application.internal.commandservices;

import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import com.textilflow.platform.profiles.domain.model.commands.*;
import com.textilflow.platform.profiles.domain.model.events.BusinessmanCreatedEvent;
import com.textilflow.platform.profiles.domain.model.events.LogoUploadedEvent;
import com.textilflow.platform.profiles.domain.model.valueobjects.*;
import com.textilflow.platform.profiles.domain.services.BusinessmanCommandService;
import com.textilflow.platform.profiles.domain.services.ProfileImageService;
import com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessmanRepository;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Businessman command service implementation
 */
@Service
public class BusinessmanCommandServiceImpl implements BusinessmanCommandService {

    private final BusinessmanRepository businessmanRepository;
    private final ProfileImageService profileImageService;
    private final ExternalIamService externalIamService;
    private final ApplicationEventPublisher eventPublisher;

    public BusinessmanCommandServiceImpl(BusinessmanRepository businessmanRepository,
                                         ProfileImageService profileImageService,
                                         ExternalIamService externalIamService,
                                         ApplicationEventPublisher eventPublisher) {
        this.businessmanRepository = businessmanRepository;
        this.profileImageService = profileImageService;
        this.externalIamService = externalIamService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Optional<Businessman> handle(CreateBusinessmanCommand command) {
        // Verify user exists in IAM
        if (!externalIamService.userExists(command.userId())) {
            throw new RuntimeException("User not found in IAM context");
        }

        // Check if businessman already exists
        if (businessmanRepository.existsByUserId(command.userId())) {
            throw new RuntimeException("Businessman profile already exists for this user");
        }

        // ✅ USAR CONSTRUCTOR QUE ACEPTA STRINGS (maneja null internamente)
        var businessman = new Businessman(
                command.userId(),
                command.companyName(),    // String - puede ser null
                command.ruc(),            // String - puede ser null
                command.businessType(),   // String - puede ser null
                command.description(),    // String - puede ser null
                command.website()         // String - puede ser null
        );

        var savedBusinessman = businessmanRepository.save(businessman);

        // Publish domain event (solo si no es null)
        var event = new BusinessmanCreatedEvent(
                savedBusinessman.getUserId(),
                savedBusinessman.getCompanyNameValue(),
                savedBusinessman.getRucValue(),
                savedBusinessman.getBusinessTypeValue()
        );
        eventPublisher.publishEvent(event);

        return Optional.of(savedBusinessman);
    }

    @Override
    public Optional<Businessman> handle(UpdateBusinessmanCommand command) {
        var businessman = businessmanRepository.findByUserId(command.userId());
        if (businessman.isEmpty()) {
            throw new RuntimeException("Businessman not found");
        }

        // ✅ CREAR VALUE OBJECTS SOLO SI NO SON NULL
        CompanyName companyName = command.companyName() != null ? new CompanyName(command.companyName()) : null;
        Ruc ruc = command.ruc() != null ? new Ruc(command.ruc()) : null;
        BusinessType businessType = command.businessType() != null ? new BusinessType(command.businessType()) : null;

        // Update businessman information
        businessman.get().updateInformation(
                companyName,
                ruc,
                businessType,
                command.description(),
                command.website()
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

        var updatedBusinessman = businessmanRepository.save(businessman.get());
        return Optional.of(updatedBusinessman);
    }

    @Override
    public Optional<Businessman> handle(UploadLogoCommand command) {
        var businessman = businessmanRepository.findByUserId(command.userId());
        if (businessman.isEmpty()) {
            throw new RuntimeException("Businessman not found");
        }

        // Delete old logo if exists
        if (businessman.get().getLogoUrlValue() != null) {
            profileImageService.deleteImage(businessman.get().getLogoUrlValue());
        }

        // Upload new logo
        String logoUrl = profileImageService.uploadImage(
                command.userId(),
                command.logoFile(),
                "BUSINESSMAN"
        );

        // Update businessman with new logo URL
        businessman.get().updateLogo(new LogoUrl(logoUrl));
        var updatedBusinessman = businessmanRepository.save(businessman.get());

        // Publish logo uploaded event
        var event = new LogoUploadedEvent(
                command.userId(),
                logoUrl,
                "BUSINESSMAN"
        );
        eventPublisher.publishEvent(event);

        return Optional.of(updatedBusinessman);
    }

    @Override
    public Optional<Businessman> handle(DeleteLogoCommand command) {
        var businessman = businessmanRepository.findByUserId(command.userId());
        if (businessman.isEmpty()) {
            throw new RuntimeException("Businessman not found");
        }

        // Delete logo from Cloudinary
        if (businessman.get().getLogoUrlValue() != null) {
            profileImageService.deleteImage(businessman.get().getLogoUrlValue());
        }

        // Update businessman with empty logo URL
        businessman.get().updateLogo(new LogoUrl());
        var updatedBusinessman = businessmanRepository.save(businessman.get());

        return Optional.of(updatedBusinessman);
    }
}