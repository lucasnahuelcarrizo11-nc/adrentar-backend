package com.example.adrentar.repository;

import com.example.adrentar.entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropietarioRepository extends JpaRepository <Propietario, Long> {

    Optional<Propietario> findByNombre(String nombre);




}
