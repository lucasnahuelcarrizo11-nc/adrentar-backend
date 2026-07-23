package com.example.adrentar.repository;

import com.example.adrentar.entity.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByProveedor_IdUsuarioOrderByFechaDesc(Long idProveedor);

    @Query("SELECT AVG(r.puntuacion) FROM Resena r WHERE r.proveedor.idUsuario = :idProveedor")
    Double calcularPromedioPorProveedor(@Param("idProveedor") Long idProveedor);

    @Query("SELECT COUNT(r) FROM Resena r WHERE r.proveedor.idUsuario = :idProveedor")
    Long contarPorProveedor(@Param("idProveedor") Long idProveedor);
}
