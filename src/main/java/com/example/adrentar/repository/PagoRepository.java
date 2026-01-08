package com.example.adrentar.repository;

import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    boolean existsByAlquilerAndMesAndAnio(
            Alquiler alquiler, Integer mes, Integer anio
    );
    Optional<Pago> findByPreferenceId(String preferenceId);
    List<Pago> findByAlquilerIdAlquiler(Long idAlquiler);

    Optional<Pago> findByExternalReference(String externalReference);
}
