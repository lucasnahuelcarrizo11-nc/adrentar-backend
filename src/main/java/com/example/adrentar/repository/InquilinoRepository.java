package com.example.adrentar.repository;

import com.example.adrentar.entity.Inquilino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquilinoRepository extends JpaRepository<Inquilino, Long> {

    Optional<Inquilino> findByNombre(String nombre);

    Optional <Inquilino> findByEmail(String email);



}
