package com.example.adrentar.repository;

import com.example.adrentar.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByAlquilerIdAlquilerOrderByFechaCreacionDesc(Long idAlquiler);
}
