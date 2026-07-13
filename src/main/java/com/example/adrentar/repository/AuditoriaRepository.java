package com.example.adrentar.repository;

import com.example.adrentar.entity.Auditoria;
import com.example.adrentar.entity.TipoAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    Optional<Auditoria> findByAlquiler_IdAlquilerAndTipo(
            Long idAlquiler,
            TipoAuditoria tipo);
}
