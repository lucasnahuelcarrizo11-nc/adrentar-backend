package com.example.adrentar.repository;

import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.Inquilino;
import com.example.adrentar.entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlquilerRepository extends JpaRepository<Alquiler,Long> {

    List<Alquiler> findByPropietarioIdUsuario(Long idPropietario);
    List<Alquiler> findByInquilinoIdUsuario(Long idInquilino);
}
