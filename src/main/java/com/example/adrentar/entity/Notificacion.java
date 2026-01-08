package com.example.adrentar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;
    private boolean leida = false;

    @ManyToOne
    @JoinColumn(name = "id_inquilino")
    private Inquilino inquilino;

    @ManyToOne
    @JoinColumn(name = "id_propietario")
    private Propietario Propietario;
}
