package com.textilflow.platform.batches.domain.model.aggregates;

import com.textilflow.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.textilflow.platform.batches.domain.model.commands.CreateBatchCommand;
import com.textilflow.platform.batches.domain.model.valueobjects.BatchStatus;
import io.jsonwebtoken.lang.Strings;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
public class Batch extends AuditableAbstractAggregateRoot<Batch> {

    private String code;
    private String client;
    private Long businessmanId;
    private Long supplierId;
    private String fabricType;
    private String color;
    private Integer quantity;
    private Double price;
    private String observations;
    private String address;
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    private BatchStatus status;

    private String imageUrl;


    public Batch() {
        this.code = Strings.EMPTY;
        this.client = Strings.EMPTY;
        this.fabricType = Strings.EMPTY;
        this.color = Strings.EMPTY;
        this.quantity = 0;
        this.price = 0.0;
        this.observations = Strings.EMPTY;
        this.address = Strings.EMPTY;
        this.date = LocalDate.now();
        this.status = BatchStatus.PENDIENTE;
        this.imageUrl = null;
    }

    public Batch updateInformation(String code, String client, Long businessmanId, Long supplierId, 
                                  String fabricType, String color, Integer quantity, Double price,
                                  String observations, String address, LocalDate date, BatchStatus status, String imageUrl) {
        this.code = code;
        this.client = client;
        this.businessmanId = businessmanId;
        this.supplierId = supplierId;
        this.fabricType = fabricType;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
        this.observations = observations;
        this.address = address;
        this.date = date;
        this.status = status;
        this.imageUrl = imageUrl;
        return this;
    }

    public Batch(CreateBatchCommand command) {
        this.code = command.code();
        this.client = command.client();
        this.businessmanId = command.businessmanId();
        this.supplierId = command.supplierId();
        this.fabricType = command.fabricType();
        this.color = command.color();
        this.quantity = command.quantity();
        this.price = command.price();
        this.observations = command.observations();
        this.address = command.address();
        this.date = command.date();
        this.status = command.status();
        this.imageUrl = command.imageUrl();
    }


}
