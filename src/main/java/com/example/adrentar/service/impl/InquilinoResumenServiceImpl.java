package com.example.adrentar.service.impl;

import com.example.adrentar.dto.ResumenInquilinoDto;
import com.example.adrentar.entity.*;
import com.example.adrentar.repository.AlquilerRepository;
import com.example.adrentar.repository.PagoRepository;
import com.example.adrentar.repository.ReporteRepository;
import com.example.adrentar.service.InquilinoResumenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;


import org.springframework.stereotype.Service;

@Service
public class InquilinoResumenServiceImpl implements InquilinoResumenService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ReporteRepository reporteRepository;

    public ResumenInquilinoDto obtenerResumen(Long idAlquiler, Long idInquilino) {
        Alquiler alquiler = alquilerRepository.findById(idAlquiler)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));

        if (alquiler.getInquilino() == null || !alquiler.getInquilino().getIdUsuario().equals(idInquilino)) {
            throw new RuntimeException("Este alquiler no pertenece al inquilino logueado");
        }

        LocalDate hoy = LocalDate.now();
        LocalDate inicio = toLocalDate(alquiler.getFechaInicio());
        LocalDate fin = toLocalDate(alquiler.getFechaFin());

        Integer progreso = null;
        Integer diasRestantes = null;
        if (inicio != null && fin != null) {
            long totalDias = ChronoUnit.DAYS.between(inicio, fin);
            long diasTranscurridos = ChronoUnit.DAYS.between(inicio, hoy);
            if (totalDias > 0) {
                long acotado = Math.max(0, Math.min(diasTranscurridos, totalDias));
                progreso = (int) Math.round((acotado * 100.0) / totalDias);
            }
            diasRestantes = (int) Math.max(0, ChronoUnit.DAYS.between(hoy, fin));
        }

        String proximoAumentoFecha = null;
        Integer diasParaProximoAumento = null;
        if (alquiler.getPorcentajeAumento() != null && alquiler.getPorcentajeAumento() > 0 && inicio != null) {
            long mesesTranscurridos = ChronoUnit.MONTHS.between(inicio, hoy);
            long periodos = mesesTranscurridos / 4;
            LocalDate proximaFecha = inicio.plusMonths((periodos + 1) * 4);
            proximoAumentoFecha = proximaFecha.toString();
            diasParaProximoAumento = (int) ChronoUnit.DAYS.between(hoy, proximaFecha);
        }

        List<Pago> pagos = pagoRepository.findByAlquilerIdAlquiler(idAlquiler);
        double totalPagado = pagos.stream()
                .filter(p -> p.getEstadoPago() == EstadoPago.APROBADO)
                .mapToDouble(p -> p.getMonto() != null ? p.getMonto() : 0.0)
                .sum();
        long pagosAprobados = pagos.stream().filter(p -> p.getEstadoPago() == EstadoPago.APROBADO).count();
        long pagosPendientes = pagos.stream().filter(p -> p.getEstadoPago() == EstadoPago.PENDIENTE).count();
        long pagosRechazados = pagos.stream().filter(p -> p.getEstadoPago() == EstadoPago.RECHAZADO).count();

        List<Reporte> reportes = reporteRepository.findByAlquilerIdAlquilerOrderByFechaCreacionDesc(idAlquiler);
        long reclamosPendientes = reportes.stream().filter(r -> r.getEstado() == EstadoReporte.PENDIENTE).count();
        long reclamosEnRevision = reportes.stream().filter(r -> r.getEstado() == EstadoReporte.EN_REVISION).count();
        long reclamosResueltos = reportes.stream().filter(r -> r.getEstado() == EstadoReporte.RESUELTO).count();

        return new ResumenInquilinoDto(
                idAlquiler, progreso, diasRestantes, proximoAumentoFecha, diasParaProximoAumento,
                totalPagado, pagosAprobados, pagosPendientes, pagosRechazados,
                reclamosPendientes, reclamosEnRevision, reclamosResueltos
        );
    }

    public LocalDate toLocalDate(Date fecha) {
        return fecha == null ? null : fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
