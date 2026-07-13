package com.example.adrentar.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data

public class AuditoriaDto {
    private Long id;
    private Long idAlquiler;
    private String tipo;
    private LocalDateTime fechaCreacion;
    private List<ImagenAuditoriaDto> imagenes;

    public AuditoriaDto(Long id, Long idAlquiler, String tipo, LocalDateTime fechaCreacion, List<ImagenAuditoriaDto> imagenes) {
        this.id = id;
        this.idAlquiler = idAlquiler;
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
        this.imagenes = imagenes;
    }
}
