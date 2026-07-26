package com.example.adrentar.repository;

import com.example.adrentar.entity.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReparacionRepository  extends JpaRepository<Reparacion, Long> {

    List<Reparacion> findByProveedorIdUsuarioOrderByFechaDesc(Long idProveedor);
}
