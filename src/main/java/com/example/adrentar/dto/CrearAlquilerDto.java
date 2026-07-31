package com.example.adrentar.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CrearAlquilerDto {
    private double precio;
    private Date fechaInicio;
    private Date fechaFin;
    private Long idPropiedad;
    private String emailInquilino;

    /** Porcentaje de aumento cada 4 meses. Opcional, null/0 = sin aumento */
    private Double porcentajeAumento;
}