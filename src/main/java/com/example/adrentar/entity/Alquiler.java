package com.example.adrentar.entity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlquiler;

    /** Precio base, sin aplicar ningún aumento */
    private double precio;

    private Date fechaInicio;
    private Date fechaFin;

    @Column(nullable = false)
    private String estado;

    @Column(name = "envelope_id")
    private String envelopeId;

    /**
     * Porcentaje que se aplica cada 4 meses sobre el precio vigente.
     * Ej: 10.0 = 10% de aumento cada 4 meses. Null o 0 = sin aumento.
     */
    @Column(name = "porcentaje_aumento")
    private Double porcentajeAumento;

    @ManyToOne
    @JoinColumn(name = "id_propietario")
    @JsonBackReference("propietario-alquileres")
    private Propietario propietario;

    @ManyToOne
    @JoinColumn(name = "id_inquilino")
    @JsonBackReference("inquilino-alquileres")
    private Inquilino inquilino;

    @ManyToOne
    @JoinColumn(name = "id_propiedad")
    @JsonBackReference
    private Propiedad propiedad;

    @OneToOne(mappedBy = "alquiler", cascade = CascadeType.ALL)
    private Contrato contrato;

    /**
     * Calcula el precio vigente a día de hoy, aplicando el porcentaje
     * de aumento compuesto cada 4 meses transcurridos desde fechaInicio.
     */
    @Transient
    public double getPrecioActual() {
        return calcularPrecioEnFecha(new Date());
    }

    /**
     * Calcula el precio vigente en una fecha de referencia dada.
     * Útil para mostrar "cuánto va a costar en tal fecha" si hiciera falta.
     */
    @Transient
    public double calcularPrecioEnFecha(Date fechaReferencia) {
        if (porcentajeAumento == null || porcentajeAumento == 0
                || fechaInicio == null || fechaReferencia == null) {
            return precio;
        }

        long mesesTranscurridos = mesesEntre(fechaInicio, fechaReferencia);
        int periodosDe4Meses = (int) (mesesTranscurridos / 4);

        if (periodosDe4Meses <= 0) {
            return precio;
        }

        return precio * Math.pow(1 + (porcentajeAumento / 100.0), periodosDe4Meses);
    }

    private long mesesEntre(Date desde, Date hasta) {
        LocalDate fechaDesde = desde.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaHasta = hasta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.MONTHS.between(fechaDesde, fechaHasta);
    }
}