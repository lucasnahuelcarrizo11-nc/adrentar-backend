package com.example.adrentar.service;

import com.example.adrentar.dto.IngresoMensualDto;
import com.example.adrentar.dto.OperacionDto;
import com.example.adrentar.entity.Alquiler;

import java.util.List;

public interface OperacionesService {

     List<OperacionDto> detalleOperaciones(Long idPropietario);

     List<IngresoMensualDto> ingresosMensuales(Long idPropietario, Integer anio);

     String calcularProximaRenovacion(Alquiler a);
}
