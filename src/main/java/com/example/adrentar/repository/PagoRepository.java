package com.example.adrentar.repository;

import com.example.adrentar.dto.IngresoMensualDto;
import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.EstadoPago;
import com.example.adrentar.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    boolean existsByAlquilerAndMesAndAnio(
            Alquiler alquiler, Integer mes, Integer anio
    );
    Optional<Pago> findByPreferenceId(String preferenceId);
    List<Pago> findByAlquilerIdAlquiler(Long idAlquiler);

    Optional<Pago> findByExternalReference(String externalReference);

    long countByAlquilerIdAlquiler(Long idAlquiler);

    long countByAlquilerIdAlquilerAndEstadoPago(Long idAlquiler, EstadoPago estadoPago);

    @Query("""
    SELECT new com.example.adrentar.dto.IngresoMensualDto(p.mes, CAST(COALESCE(SUM(p.monto), 0.0) AS double))
    FROM Pago p
    WHERE p.alquiler.propietario.idUsuario = :idPropietario
      AND p.anio = :anio
      AND p.estadoPago = com.example.adrentar.entity.EstadoPago.APROBADO
    GROUP BY p.mes
    ORDER BY p.mes
    """)
    List<IngresoMensualDto> ingresosMensuales(@Param("idPropietario") Long idPropietario, @Param("anio") Integer anio);

    Pago findByAlquilerAndMesAndAnio(Alquiler alquiler, int mes, int anio);
}
