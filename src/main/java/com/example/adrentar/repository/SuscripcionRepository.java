package com.example.adrentar.repository;

import com.example.adrentar.entity.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    Optional<Suscripcion> findByUsuario_IdUsuario(Long idUsuario);

    Optional<Suscripcion> findByPreapprovalId(String preapprovalId);

}
