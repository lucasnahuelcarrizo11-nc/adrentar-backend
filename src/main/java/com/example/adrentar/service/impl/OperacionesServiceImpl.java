package com.example.adrentar.service.impl;

import com.example.adrentar.dto.IngresoMensualDto;
import com.example.adrentar.dto.OperacionDto;
import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.EstadoPago;
import com.example.adrentar.repository.AlquilerRepository;
import com.example.adrentar.repository.PagoRepository;
import com.example.adrentar.repository.ReporteRepository;
import com.example.adrentar.service.OperacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class OperacionesServiceImpl implements OperacionesService {

    private static final String[] MESES = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ReporteRepository reporteRepository;

    public List<OperacionDto> detalleOperaciones(Long idPropietario) {
        List<Alquiler> alquileres = alquilerRepository.findByPropietarioIdUsuario(idPropietario);
        List<OperacionDto> resultado = new ArrayList<>();

        for (Alquiler a : alquileres) {
            long pagosTotales = pagoRepository.countByAlquilerIdAlquiler(a.getIdAlquiler());
            long pagosRealizados = pagoRepository.countByAlquilerIdAlquilerAndEstadoPago(
                    a.getIdAlquiler(), EstadoPago.APROBADO);
            long reclamos = reporteRepository.countByAlquilerIdAlquiler(a.getIdAlquiler());

            LocalDate fechaInicio = a.getFechaInicio() == null ? null
                    : a.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            resultado.add(new OperacionDto(
                    a.getIdAlquiler(),
                    fechaInicio != null ? fechaInicio.getYear() : null,
                    fechaInicio != null ? MESES[fechaInicio.getMonthValue() - 1] : null,
                    a.getPropiedad() != null ? a.getPropiedad().getTituloPropiedad() : null,
                    a.getInquilino() != null ? a.getInquilino().getEmail() : null,
                    a.getPrecio(),
                    a.getEstado(),
                    pagosRealizados,
                    pagosTotales,
                    reclamos,
                    calcularProximaRenovacion(a)
            ));
        }

        return resultado;
    }

    public List<IngresoMensualDto> ingresosMensuales(Long idPropietario, Integer anio) {
        return pagoRepository.ingresosMensuales(idPropietario, anio);
    }

    public String calcularProximaRenovacion(Alquiler a) {
        if (a.getFechaFin() == null) {
            return "Sin definir";
        }

        LocalDate hoy = LocalDate.now();
        LocalDate fechaFin = a.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (!fechaFin.isAfter(hoy)) {
            return "Completado";
        }

        long dias = ChronoUnit.DAYS.between(hoy, fechaFin);
        if (dias <= 90) {
            return dias + " días";
        }

        long meses = ChronoUnit.MONTHS.between(hoy, fechaFin);
        return meses + " meses";
    }
}