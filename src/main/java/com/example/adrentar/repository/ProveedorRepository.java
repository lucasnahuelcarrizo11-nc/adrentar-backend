package com.example.adrentar.repository;

import com.example.adrentar.entity.Propietario;
import com.example.adrentar.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByNombreCompleto(String nombreCompleto);


}
