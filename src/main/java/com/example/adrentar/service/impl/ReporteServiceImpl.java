package com.example.adrentar.service.impl;

import com.example.adrentar.dto.ReporteDto;
import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.EstadoReporte;
import com.example.adrentar.entity.Reporte;
import com.example.adrentar.repository.AlquilerRepository;
import com.example.adrentar.repository.ReporteRepository;
import com.example.adrentar.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private NotificacionServiceImpl notificacionServiceImpl;
    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Override
    public ReporteDto crearReporte(ReporteDto dto) {
        Alquiler alquiler = alquilerRepository.findById(dto.getIdAlquiler())
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));

        if (!alquiler.getEstado().equals("ACEPTADO")) {
            throw new RuntimeException("El alquiler no está activo");
        }

        Reporte reporte = new Reporte();
        reporte.setAlquiler(alquiler);
        reporte.setTitulo(dto.getTitulo());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setEstado(EstadoReporte.PENDIENTE);
        reporte.setFechaCreacion(LocalDateTime.now());

        Reporte guardado = reporteRepository.save(reporte);

        notificacionServiceImpl.notificarPropietario(
                alquiler.getPropietario(),
                "El inquilino realizo un reporte en "+alquiler.getPropiedad().getDireccion()  + ": " + dto.getDescripcion()
        );

        return mapToDto(guardado);


    }

    @Override
    public List<ReporteDto> obtenerPorAlquiler(Long idAlquiler) {
        return reporteRepository
                .findByAlquilerIdAlquilerOrderByFechaCreacionDesc(idAlquiler)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ReporteDto cambiarEstado(Long idReporte, String estado) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        reporte.setEstado(EstadoReporte.valueOf(estado));
        reporte.setFechaActualizacion(LocalDateTime.now());

        Reporte actualizado = reporteRepository.save(reporte);

        return mapToDto(actualizado);

    }

    private ReporteDto mapToDto(Reporte reporte) {
        ReporteDto dto = new ReporteDto();
        dto.setIdReporte(reporte.getIdReporte());
        dto.setIdAlquiler(reporte.getAlquiler().getIdAlquiler());
        dto.setTitulo(reporte.getTitulo());
        dto.setDescripcion(reporte.getDescripcion());
        dto.setEstado(reporte.getEstado().name());
        dto.setFechaCreacion(reporte.getFechaCreacion());
        return dto;
    }
}
