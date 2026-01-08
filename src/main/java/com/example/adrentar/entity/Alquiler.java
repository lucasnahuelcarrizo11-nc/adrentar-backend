package com.example.adrentar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlquiler;

    private double precio;
    private Date fechaInicio;
    private Date fechaFin;


    @Column(nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_propietario")
    @JsonBackReference("propietario-alquileres")
    private Propietario propietario;

    @ManyToOne
    @JoinColumn(name = "id_inquilino")
    @JsonBackReference("inquilino-alquileres")
    private Inquilino inquilino;

    @ManyToOne
    @JoinColumn(name = "id_propiedad")
    @JsonBackReference
    private Propiedad propiedad;
}
