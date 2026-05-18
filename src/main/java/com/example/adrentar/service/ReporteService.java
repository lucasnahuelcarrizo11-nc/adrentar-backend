package com.example.adrentar.service;

import com.example.adrentar.dto.ReporteDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ReporteService {
    ReporteDto crearReporte(ReporteDto dto);
    List<ReporteDto> obtenerPorAlquiler(Long idAlquiler);
    ReporteDto cambiarEstado(Long idReporte, String estado);
}
