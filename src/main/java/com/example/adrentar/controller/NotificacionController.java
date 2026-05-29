package com.example.adrentar.controller;

import com.example.adrentar.entity.Notificacion;
import com.example.adrentar.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionRepository notificacionRepository;

    // El front llama esto al cargar el header/campana
    @GetMapping("/inquilino/{id}")
    public List<Notificacion> getDeInquilino(@PathVariable Long id) {
        return notificacionRepository.findByInquilinoIdUsuario(id);
    }

    @GetMapping("/propietario/{id}")
    public List<Notificacion> getDePropietario(@PathVariable Long id) {
        return notificacionRepository.findByPropietarioIdUsuario(id);
    }

    // Marcar como leída
    @PatchMapping("/{id}/leer")
    public void marcarLeida(@PathVariable Long id) {
        notificacionRepository.findById(id).ifPresent(n -> {
            n.setLeida(true);
            notificacionRepository.save(n);
        });
    }
}
