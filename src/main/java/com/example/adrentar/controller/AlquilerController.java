package com.example.adrentar.controller;


import com.example.adrentar.dto.CrearAlquilerDto;
import com.example.adrentar.entity.*;
import com.example.adrentar.repository.*;
import com.example.adrentar.service.EmailService;
import com.example.adrentar.service.PropietarioService;
import com.example.adrentar.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private InquilinoRepository inquilinoRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    // 🟢 Crear alquiler (propietario logueado)
    @PostMapping("/crear")
    public ResponseEntity<?> crearAlquiler(
            @RequestHeader("Authorization") String token,
            @RequestBody CrearAlquilerDto dto) {

        // 🔥 LIMPIAR TOKEN
        String tokenLimpio = token.replace("Bearer ", "").trim();

        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorToken(tokenLimpio);

        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Propietario propietario)) {
            return ResponseEntity.status(401).body("Solo un propietario puede crear alquileres.");
        }

        Propiedad propiedad = propiedadRepository.findById(dto.getIdPropiedad())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        if (!propiedad.getPropietario().getIdUsuario().equals(propietario.getIdUsuario())) {
            return ResponseEntity.status(403)
                    .body("No puedes crear alquileres en propiedades que no son tuyas.");
        }

        Inquilino inquilino = inquilinoRepository.findByEmail(dto.getEmailInquilino())
                .orElseThrow(() -> new RuntimeException("No existe un inquilino con ese email."));

        Alquiler alquiler = new Alquiler();
        alquiler.setPrecio(dto.getPrecio());
        alquiler.setFechaInicio(dto.getFechaInicio());
        alquiler.setFechaFin(dto.getFechaFin());
        alquiler.setPropietario(propietario);
        alquiler.setPropiedad(propiedad);
        alquiler.setInquilino(inquilino);
        alquiler.setEstado("PENDIENTE");

        alquilerRepository.save(alquiler);

        Notificacion noti = new Notificacion();
        noti.setMensaje("Nueva solicitud de alquiler para " + propiedad.getDireccion());
        noti.setInquilino(inquilino);
        notificacionRepository.save(noti);

        String asunto = "Nueva solicitud de alquiler";
        String cuerpo = "Hola " + inquilino.getNombre() + ",\n\n"
                + "El propietario " + propietario.getNombre()
                + " te envió una solicitud de alquiler.\n\n"
                + "Propiedad: " + propiedad.getDireccion() + "\n"
                + "Precio: $" + dto.getPrecio() + "\n"
                + "Desde: " + dto.getFechaInicio() + "\n"
                + "Hasta: " + dto.getFechaFin() + "\n\n"
                + "Ingresá a Adrentar para aceptarla o rechazarla.\n\n"
                + "Saludos,\nEquipo Adrentar";

        emailService.enviarCorreo(inquilino.getEmail(), asunto, cuerpo);

        return ResponseEntity.ok("Alquiler creado correctamente.");
    }

    @GetMapping("/mis-alquileres")
    public ResponseEntity<?> obtenerMisAlquileres(@RequestHeader("Authorization") String token) {

        // 🔥 LIMPIAR TOKEN
        String tokenLimpio = token.replace("Bearer ", "").trim();

        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorToken(tokenLimpio);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido o usuario no encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario instanceof Propietario propietario) {
            return ResponseEntity.ok(
                    alquilerRepository.findByPropietarioIdUsuario(propietario.getIdUsuario())
            );
        }

        if (usuario instanceof Inquilino inquilino) {
            return ResponseEntity.ok(
                    alquilerRepository.findByInquilinoIdUsuario(inquilino.getIdUsuario())
            );
        }

        return ResponseEntity.status(400).body("Tipo de usuario no válido.");
    }

    @PutMapping("/{id}/aceptar")
    public ResponseEntity<?> aceptarAlquiler(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        String tokenLimpio = token.replace("Bearer ", "").trim();
        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorToken(tokenLimpio);

        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Inquilino inquilino)) {
            return ResponseEntity.status(401).body("Solo un inquilino puede aceptar alquileres.");
        }

        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado."));

        if (!alquiler.getInquilino().getIdUsuario().equals(inquilino.getIdUsuario())) {
            return ResponseEntity.status(403).body("No puedes aceptar un alquiler que no es tuyo.");
        }

        alquiler.setEstado("ACEPTADO");
        alquilerRepository.save(alquiler);

        // Crear notificación para el propietario
        Notificacion noti = new Notificacion();
        noti.setMensaje("El inquilino aceptó el alquiler de la propiedad "
                + alquiler.getPropiedad().getDireccion());
        noti.setPropietario(alquiler.getPropietario());
        notificacionRepository.save(noti);

        return ResponseEntity.ok("Alquiler aceptado.");
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarAlquiler(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        String tokenLimpio = token.replace("Bearer ", "").trim();
        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorToken(tokenLimpio);

        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Inquilino inquilino)) {
            return ResponseEntity.status(401).body("Solo un inquilino puede rechazar alquileres.");
        }

        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado."));

        if (!alquiler.getInquilino().getIdUsuario().equals(inquilino.getIdUsuario())) {
            return ResponseEntity.status(403).body("No puedes rechazar un alquiler que no es tuyo.");
        }

        alquiler.setEstado("RECHAZADO");
        alquilerRepository.save(alquiler);

        Notificacion noti = new Notificacion();
        noti.setMensaje("El inquilino rechazó el alquiler de la propiedad "
                + alquiler.getPropiedad().getDireccion());
        noti.setPropietario(alquiler.getPropietario());
        notificacionRepository.save(noti);

        return ResponseEntity.ok("Alquiler rechazado.");
    }


}
