package com.example.adrentar.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImagenAuditoriaDto {
    private Long id;
    private String url;
    private LocalDateTime fechaCarga;

    public ImagenAuditoriaDto() {
    }

    public ImagenAuditoriaDto(Long id, String url, LocalDateTime fechaCarga) {
        this.id = id;
        this.url = url;
        this.fechaCarga = fechaCarga;
    }
}