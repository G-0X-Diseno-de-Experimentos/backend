package com.textilflow.platform.batches.application.internal.commandservices;

import com.textilflow.platform.batches.domain.model.aggregates.Batch;
import com.textilflow.platform.batches.domain.model.commands.CreateBatchCommand;
import com.textilflow.platform.batches.domain.model.commands.DeleteBatchCommand;
import com.textilflow.platform.batches.domain.model.commands.UpdateBatchCommand;
import com.textilflow.platform.batches.domain.model.commands.UpdateBatchImageCommand;
import com.textilflow.platform.batches.domain.model.commands.DeleteBatchImageCommand;
import com.textilflow.platform.batches.domain.model.events.BatchCreatedEvent;
import com.textilflow.platform.batches.domain.model.events.BatchUpdatedEvent;
import com.textilflow.platform.batches.domain.services.BatchCommandService;
import com.textilflow.platform.batches.infraestructure.persistence.repositories.BatchRepository;
import com.textilflow.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Batch command service implementation
 */
@Service
public class BatchCommandServiceImpl implements BatchCommandService {

    private final BatchRepository batchRepository;
    private final ProfilesContextFacade profilesContextFacade;
    private final ApplicationEventPublisher eventPublisher;

    public BatchCommandServiceImpl(BatchRepository batchRepository,
                                   ProfilesContextFacade profilesContextFacade,
                                   ApplicationEventPublisher eventPublisher) {
        this.batchRepository = batchRepository;
        this.profilesContextFacade = profilesContextFacade;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Long handle(CreateBatchCommand command) {
        // Check if batch already exists with same code
        if (batchRepository.existsByCode(command.code())) {
            throw new RuntimeException("Batch with code already exists");
        }

        // Validate businessman and supplier exist using ACL
        System.out.println("Validating businessman with userId: " + command.businessmanId());
        var businessmanId = profilesContextFacade.getBusinessmanByUserId(command.businessmanId());
        System.out.println("Found businessmanId: " + businessmanId);
        if (businessmanId == null) {
            throw new RuntimeException("Businessman not found");
        }
        
        System.out.println("Validating supplier with userId: " + command.supplierId());
        var supplierId = profilesContextFacade.getSupplierByUserId(command.supplierId());
        System.out.println("Found supplierId: " + supplierId);
        if (supplierId == null) {
            throw new RuntimeException("Supplier not found");
        }

        var batch = new Batch(command);
        var savedBatch = batchRepository.save(batch);

        // Publish domain event
        var event = new BatchCreatedEvent(
                savedBatch.getId(),
                command.code(),
                command.client(),
                command.businessmanId(),
                command.supplierId(),
                command.fabricType(),
                command.color(),
                command.quantity(),
                command.price(),
                command.observations(),
                command.address(),
                command.date(),
                command.status(),
                command.imageUrl()
        );
        eventPublisher.publishEvent(event);

        return savedBatch.getId();
    }

    @Override
    public Optional<Batch> handle(UpdateBatchCommand command) {
        var batch = batchRepository.findById(command.batchId());
        if (batch.isEmpty()) {
            throw new RuntimeException("Batch not found");
        }

        // Check if another batch exists with same code
        if (batchRepository.existsByCodeAndIdIsNot(command.code(), command.batchId())) {
            throw new RuntimeException("Another batch with this code already exists");
        }

        // Validate businessman and supplier exist using ACL
        var businessmanId = profilesContextFacade.getBusinessmanByUserId(command.businessmanId());
        if (businessmanId == null) {
            throw new RuntimeException("Businessman not found");
        }
        
        var supplierId = profilesContextFacade.getSupplierByUserId(command.supplierId());
        if (supplierId == null) {
            throw new RuntimeException("Supplier not found");
        }

        // Update batch information
        batch.get().updateInformation(
                command.code(),
                command.client(),
                command.businessmanId(),
                command.supplierId(),
                command.fabricType(),
                command.color(),
                command.quantity(),
                command.price(),
                command.observations(),
                command.address(),
                command.date(),
                command.status(),
                command.imageUrl()
        );

        var updatedBatch = batchRepository.save(batch.get());

        // Publish domain event
        var event = new BatchUpdatedEvent(
                updatedBatch.getId(),
                command.code(),
                command.client(),
                command.businessmanId(),
                command.supplierId(),
                command.fabricType(),
                command.color(),
                command.quantity(),
                command.price(),
                command.observations(),
                command.address(),
                command.date(),
                command.status(),
                command.imageUrl()
        );
        eventPublisher.publishEvent(event);

        return Optional.of(updatedBatch);
    }

    @Override
    public void handle(DeleteBatchCommand command) {
        if (!batchRepository.existsById(command.batchId())) {
            throw new RuntimeException("Batch not found");
        }
        
        batchRepository.deleteById(command.batchId());
    }

    @Override
    public Optional<Batch> handle(UpdateBatchImageCommand command) {
        var batch = batchRepository.findById(command.batchId());
        if (batch.isEmpty()) {
            throw new RuntimeException("Batch not found");
        }

        var currentBatch = batch.get();
        currentBatch.updateInformation(
                currentBatch.getCode(),
                currentBatch.getClient(),
                currentBatch.getBusinessmanId(),
                currentBatch.getSupplierId(),
                currentBatch.getFabricType(),
                currentBatch.getColor(),
                currentBatch.getQuantity(),
                currentBatch.getPrice(),
                currentBatch.getObservations(),
                currentBatch.getAddress(),
                currentBatch.getDate(),
                currentBatch.getStatus(),
                command.imageUrl()
        );

        var updatedBatch = batchRepository.save(currentBatch);
        return Optional.of(updatedBatch);
    }

    @Override
    public Optional<Batch> handle(DeleteBatchImageCommand command) {
        var batch = batchRepository.findById(command.batchId());
        if (batch.isEmpty()) {
            throw new RuntimeException("Batch not found");
        }

        var currentBatch = batch.get();
        currentBatch.updateInformation(
                currentBatch.getCode(),
                currentBatch.getClient(),
                currentBatch.getBusinessmanId(),
                currentBatch.getSupplierId(),
                currentBatch.getFabricType(),
                currentBatch.getColor(),
                currentBatch.getQuantity(),
                currentBatch.getPrice(),
                currentBatch.getObservations(),
                currentBatch.getAddress(),
                currentBatch.getDate(),
                currentBatch.getStatus(),
                ""
        );

        var updatedBatch = batchRepository.save(currentBatch);
        return Optional.of(updatedBatch);
    }

}
