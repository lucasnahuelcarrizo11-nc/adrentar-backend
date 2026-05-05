package com.example.adrentar.service.impl;

import com.example.adrentar.dto.AlquilerListadoDto;
import com.example.adrentar.dto.CrearAlquilerDto;
import com.example.adrentar.entity.*;
import com.example.adrentar.repository.*;
import com.example.adrentar.service.AlquilerService;
import com.example.adrentar.service.EmailService;
import com.example.adrentar.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlquilerServiceImpl implements AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

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

    /* ===============================
       CREAR ALQUILER
    ================================ */
    @Override
    public void crearAlquiler(String token, CrearAlquilerDto dto) {

        String tokenLimpio = token.replace("Bearer ", "").trim();

        Usuario usuario = usuarioService.getUsuarioPorToken(tokenLimpio)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (!(usuario instanceof Propietario propietario)) {
            throw new RuntimeException("Solo un propietario puede crear alquileres");
        }

        Propiedad propiedad = propiedadRepository.findById(dto.getIdPropiedad())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        if (!propiedad.getPropietario().getIdUsuario()
                .equals(propietario.getIdUsuario())) {
            throw new RuntimeException("La propiedad no pertenece al propietario");
        }

        Inquilino inquilino = inquilinoRepository.findByEmail(dto.getEmailInquilino())
                .orElseThrow(() -> new RuntimeException("Inquilino no encontrado"));

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

        emailService.enviarCorreo(
                inquilino.getEmail(),
                "Nueva solicitud de alquiler",
                "Tenés una nueva solicitud de alquiler en Adrentar, Para completar la solicitud Tenes que ingresar a https://adrentar-frontend.vercel.app/"
        );
    }

    /* ===============================
       LISTAR MIS ALQUILERES (FIX)
    ================================ */
    @Override
    public List<AlquilerListadoDto> obtenerMisAlquileres(String token) {

        String tokenLimpio = token.replace("Bearer ", "").trim();

        Usuario usuario = usuarioService.getUsuarioPorToken(tokenLimpio)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        List<Alquiler> alquileres;

        if (usuario instanceof Propietario p) {
            alquileres = alquilerRepository
                    .findByPropietarioIdUsuario(p.getIdUsuario());
        } else if (usuario instanceof Inquilino i) {
            alquileres = alquilerRepository
                    .findByInquilinoIdUsuario(i.getIdUsuario());
        } else {
            throw new RuntimeException("Tipo de usuario no válido");
        }

        return alquileres.stream().map(a -> {
            AlquilerListadoDto dto = new AlquilerListadoDto();

            dto.setIdAlquiler(a.getIdAlquiler());
            dto.setPrecio(a.getPrecio());
            dto.setFechaInicio(a.getFechaInicio());
            dto.setFechaFin(a.getFechaFin());
            dto.setEstado(a.getEstado());

            dto.setDireccionPropiedad(
                    a.getPropiedad().getDireccion()
            );

            dto.setNombreInquilino(
                    a.getInquilino().getNombre()
            );

            dto.setApellidoInquilino(
                    a.getInquilino().getApellido()
            );

            dto.setApellidoPropietario(a.getPropietario().getApellido());
            dto.setNombrePropietario(a.getPropietario().getNombre());

            return dto;
        }).toList();
    }

    /* ===============================
       ACEPTAR ALQUILER
    ================================ */
    @Override
    public void aceptarAlquiler(String token, Long idAlquiler) {

        String tokenLimpio = token.replace("Bearer ", "").trim();

        Usuario usuario = usuarioService.getUsuarioPorToken(tokenLimpio)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (!(usuario instanceof Inquilino)) {
            throw new RuntimeException("Solo un inquilino puede aceptar");
        }

        Alquiler alquiler = alquilerRepository.findById(idAlquiler)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));

        alquiler.setEstado("ACEPTADO");
        alquilerRepository.save(alquiler);
    }

    /* ===============================
       RECHAZAR ALQUILER
    ================================ */
    @Override
    public void rechazarAlquiler(String token, Long idAlquiler) {

        String tokenLimpio = token.replace("Bearer ", "").trim();

        Usuario usuario = usuarioService.getUsuarioPorToken(tokenLimpio)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (!(usuario instanceof Inquilino)) {
            throw new RuntimeException("Solo un inquilino puede rechazar");
        }

        Alquiler alquiler = alquilerRepository.findById(idAlquiler)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));

        alquiler.setEstado("RECHAZADO");
        alquilerRepository.save(alquiler);
    }
    /* ===============================
   CANCELAR ALQUILER
================================ */
    @Override
    public void cancelarAlquiler(String token, Long idAlquiler) {

        String tokenLimpio = token.replace("Bearer ", "").trim();

        Usuario usuario = usuarioService.getUsuarioPorToken(tokenLimpio)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        Alquiler alquiler = alquilerRepository.findById(idAlquiler)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));

        // Tanto inquilino como propietario pueden cancelar, pero solo el suyo
        if (usuario instanceof Inquilino i) {
            if (!alquiler.getInquilino().getIdUsuario().equals(i.getIdUsuario())) {
                throw new RuntimeException("No tenés permiso para cancelar este alquiler");
            }
        } else if (usuario instanceof Propietario p) {
            if (!alquiler.getPropietario().getIdUsuario().equals(p.getIdUsuario())) {
                throw new RuntimeException("No tenés permiso para cancelar este alquiler");
            }
        } else {
            throw new RuntimeException("Tipo de usuario no válido");
        }

        if (alquiler.getEstado().equals("CANCELADO")) {
            throw new RuntimeException("El alquiler ya fue cancelado");
        }

        alquiler.setEstado("CANCELADO");
        alquilerRepository.save(alquiler);

        // Notificar a la otra parte
        String mensajeNoti;
        String emailDestino;

        if (usuario instanceof Inquilino) {
            // Canceló el inquilino → notificar al propietario
            mensajeNoti = "El inquilino canceló el alquiler de " + alquiler.getPropiedad().getDireccion();
            emailDestino = alquiler.getPropietario().getEmail();
        } else {
            // Canceló el propietario → notificar al inquilino
            mensajeNoti = "El propietario canceló el alquiler de " + alquiler.getPropiedad().getDireccion();

            Notificacion noti = new Notificacion();
            noti.setMensaje(mensajeNoti);
            noti.setInquilino(alquiler.getInquilino());
            notificacionRepository.save(noti);

            emailDestino = alquiler.getInquilino().getEmail();
        }

        emailService.enviarCorreo(
                emailDestino,
                "Alquiler cancelado",
                mensajeNoti + ". Ingresá a Adrentar para más detalles."
        );
    }
}
