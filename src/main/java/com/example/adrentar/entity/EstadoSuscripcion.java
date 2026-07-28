package com.example.adrentar.entity;

public enum EstadoSuscripcion {
    TRIAL,                    // Período de prueba de 30 días
    PENDIENTE_AUTORIZACION,   // Usuario inició el checkout de MP pero todavía no autorizó la tarjeta
    ACTIVA,                   // Suscripción cobrando automáticamente cada mes
    VENCIDA,                  // Trial terminado sin suscripción, o suscripción pausada
    CANCELADA                 // Usuario canceló la suscripción
}