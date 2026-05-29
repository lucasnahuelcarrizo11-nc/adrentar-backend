package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Inquilino;
import com.example.adrentar.entity.Notificacion;
import com.example.adrentar.entity.Propietario;
import com.example.adrentar.repository.NotificacionRepository;
import com.example.adrentar.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {
    private final NotificacionRepository notificacionRepository;

    @Override
    public void notificarInquilino(Inquilino inquilino, String mensaje) {
        Notificacion noti = new Notificacion();
        noti.setMensaje(mensaje);
        noti.setInquilino(inquilino);
        notificacionRepository.save(noti);
    }

    @Override
    public void notificarPropietario(Propietario propietario, String mensaje) {
        Notificacion noti = new Notificacion();
        noti.setMensaje(mensaje);
        noti.setPropietario(propietario);
        notificacionRepository.save(noti);
    }

    public void notificarAmbos(Inquilino inquilino, Propietario propietario,
                               String mensajeInquilino, String mensajePropietario) {
        notificarInquilino(inquilino, mensajeInquilino);
        notificarPropietario(propietario, mensajePropietario);
    }
}
