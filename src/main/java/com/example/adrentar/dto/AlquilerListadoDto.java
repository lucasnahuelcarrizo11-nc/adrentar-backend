package com.example.adrentar.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AlquilerListadoDto {

    private Long idAlquiler;
    private String direccionPropiedad;

    private String nombrePropietario;
    private String apellidoPropietario;

    private String nombreInquilino;
    private String apellidoInquilino;

    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    private double precio;
}