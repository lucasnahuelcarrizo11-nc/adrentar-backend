package com.example.adrentar.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data

public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alquiler_id", nullable = false)
    private Alquiler alquiler;

    @Enumerated(EnumType.STRING)
    private TipoAuditoria tipo;

    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "auditoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenAuditoria> imagenes = new ArrayList<>();

}
