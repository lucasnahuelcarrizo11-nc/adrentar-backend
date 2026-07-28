package com.example.adrentar.dto;

import com.example.adrentar.entity.EstadoSuscripcion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionStatusDto {
    private EstadoSuscripcion estado;
    private LocalDate fechaFinTrial;
    private long diasRestantesTrial;
    private boolean accesoActivo;
    private LocalDate fechaProximoPago;
}