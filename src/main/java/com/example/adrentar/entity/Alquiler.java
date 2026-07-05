package com.example.adrentar.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @OneToOne(mappedBy = "alquiler", cascade = CascadeType.ALL)
    private Contrato contrato;
}
