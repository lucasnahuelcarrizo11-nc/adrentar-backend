package com.example.adrentar.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ImagenAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auditoria_id", nullable = false)
    private Auditoria auditoria;

    private String nombreArchivo;
    private String url;
    private LocalDateTime fechaCarga;
}
