package com.example.adrentar.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReporteDto {
    private Long idReporte;
    private Long idAlquiler;
    private String titulo;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaCreacion;

}
