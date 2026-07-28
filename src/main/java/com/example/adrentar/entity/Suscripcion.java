package com.example.adrentar.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "suscripciones")
@Data
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSuscripcion;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSuscripcion estado;

    @Column(nullable = false)
    private LocalDate fechaInicioTrial;

    @Column(nullable = false)
    private LocalDate fechaFinTrial;

    // ID de la suscripción (preapproval) en MercadoPago, una vez creada
    @Column(nullable = true)
    private String preapprovalId;

    // Informativo — la fecha real la maneja MercadoPago internamente
    @Column(nullable = true)
    private LocalDate fechaProximoPago;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = true)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}