package com.example.adrentar.entity;

import com.example.adrentar.enums.EstadoContrato;
import com.example.adrentar.enums.TipoContrato;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrato;

    @OneToOne
    @JoinColumn(name = "id_alquiler", nullable = false)
    private Alquiler alquiler;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoContrato estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoContrato tipo;

    @Column(nullable = false)
    private String nombreArchivo;

    private String rutaArchivoOriginal;

    private String rutaArchivoFirmado;

    private String envelopeId;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaFirma;

}