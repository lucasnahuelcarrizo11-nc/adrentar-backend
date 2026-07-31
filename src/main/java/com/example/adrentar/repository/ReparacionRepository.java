package com.example.adrentar.repository;


import com.example.adrentar.dto.GastoPropiedadDto;
import com.example.adrentar.entity.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Long> {

    List<Reparacion> findByProveedorIdUsuarioOrderByFechaDesc(Long idProveedor);

    @Query("""
    SELECT new com.example.adrentar.dto.GastoPropiedadDto(
        p.idPropiedad, p.TituloPropiedad, COUNT(r), CAST(COALESCE(SUM(r.monto), 0.0) AS double)
    )
    FROM Propiedad p
    LEFT JOIN Reparacion r ON r.propiedad = p
        AND (:anio IS NULL OR FUNCTION('YEAR', r.fecha) = :anio)
        AND (:mes IS NULL OR FUNCTION('MONTH', r.fecha) = :mes)
    WHERE p.propietario.idUsuario = :idPropietario
    GROUP BY p.idPropiedad, p.TituloPropiedad
    ORDER BY p.TituloPropiedad
    """)
    List<GastoPropiedadDto> resumenGastosPorPropietario(
            @Param("idPropietario") Long idPropietario,
            @Param("anio") Integer anio,
            @Param("mes") Integer mes
    );

}