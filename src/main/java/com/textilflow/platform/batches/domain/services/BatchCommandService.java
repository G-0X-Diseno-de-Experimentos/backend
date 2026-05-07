package com.textilflow.platform.batches.domain.services;

import com.textilflow.platform.batches.domain.model.aggregates.Batch;
import com.textilflow.platform.batches.domain.model.commands.CreateBatchCommand;
import com.textilflow.platform.batches.domain.model.commands.DeleteBatchCommand;
import com.textilflow.platform.batches.domain.model.commands.UpdateBatchCommand;
import com.textilflow.platform.batches.domain.model.commands.UpdateBatchImageCommand;
import com.textilflow.platform.batches.domain.model.commands.DeleteBatchImageCommand;

import java.util.Optional;

public interface BatchCommandService {

    Long handle(CreateBatchCommand command);

    Optional<Batch> handle(UpdateBatchCommand command);

    Optional<Batch> handle(UpdateBatchImageCommand command);

    Optional<Batch> handle(DeleteBatchImageCommand command);

    void handle(DeleteBatchCommand command);


}
