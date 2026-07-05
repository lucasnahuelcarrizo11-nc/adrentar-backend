package com.example.adrentar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.adrentar.entity.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    Optional<Contrato> findByAlquilerIdAlquiler(Long idAlquiler);

}