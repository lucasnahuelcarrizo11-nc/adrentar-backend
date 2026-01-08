package com.example.adrentar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDto {
    private int mes;
    private int anio;
    private String estado;
}