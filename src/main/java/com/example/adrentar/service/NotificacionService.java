package com.example.adrentar.service;

import com.example.adrentar.entity.Inquilino;
import com.example.adrentar.entity.Propietario;

public interface NotificacionService {

     void notificarInquilino(Inquilino inquilino, String mensaje);

    void notificarPropietario(Propietario propietario, String mensaje);

    void notificarAmbos(Inquilino inquilino, Propietario propietario,
                        String mensajeInquilino, String mensajePropietario);
}
