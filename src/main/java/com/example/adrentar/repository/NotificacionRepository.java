package com.example.adrentar.repository;

import com.example.adrentar.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByInquilinoIdUsuarioOrderByIdDesc(Long idInquilino);
}
