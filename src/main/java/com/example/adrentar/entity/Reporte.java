package com.example.adrentar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReporte;

    @ManyToOne
    @JoinColumn(name = "id_alquiler")
    private Alquiler alquiler;

    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoReporte estado; // PENDIENTE, EN_REVISION, RESUELTO

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
