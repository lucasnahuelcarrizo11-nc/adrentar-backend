package com.example.adrentar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlquilerCreadoDto {
    private Long idAlquiler;
    private String emailInquilino;
    private String nombreInquilino;
    private String direccionPropiedad;
    private String fechaInicio;
    private String fechaFin;
    private Double precio;
}