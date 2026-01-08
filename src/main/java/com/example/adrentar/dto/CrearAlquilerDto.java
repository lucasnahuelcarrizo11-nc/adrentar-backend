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
}
