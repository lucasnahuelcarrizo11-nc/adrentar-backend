package com.example.adrentar.repository;

import com.example.adrentar.entity.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    Optional<Propiedad> findByIdPropiedad(Long idPropiedad);

    Optional<Propiedad> findByDireccion(String direccion);

    boolean existsByDireccion(String direccion);

    List<Propiedad> findByPropietarioIdUsuario(Long idUsuario);
}
