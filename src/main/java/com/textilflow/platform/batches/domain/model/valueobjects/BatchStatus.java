package com.textilflow.platform.batches.domain.model.valueobjects;

public enum BatchStatus {
    PENDIENTE("Pendiente"),
    ACEPTADO("Aceptado"),
    RECHAZADO("Rechazado"),
    COMPLETADO("Completado"),
    POR_ENVIAR("Por enviar"),
    ENVIADO("Enviado");

    private final String displayName;

    BatchStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}