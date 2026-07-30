package com.example.adrentar.service;

import com.example.adrentar.dto.SuscripcionStatusDto;
import com.example.adrentar.entity.Usuario;

public interface SuscripcionService {



    // Se llama una sola vez, al registrar un usuario nuevo
    void iniciarTrial(Usuario usuario);

    // Estado completo para mostrar en el frontend
    SuscripcionStatusDto obtenerEstado(Long idUsuario);

    // Chequeo rápido de acceso, para usar como guard en otros controllers
    boolean tieneAccesoActivo(Long idUsuario);

    // Crea la suscripción (preapproval) en MercadoPago y devuelve la URL de checkout
    String crearSuscripcionMP(Long idUsuario) throws Exception;

    // Llamado desde el webhook cuando MP notifica un cambio de estado del preapproval
    void procesarWebhookPreapproval(String preapprovalId) throws Exception;

    void cancelarSuscripcion(Long idUsuario) throws Exception;

    void sincronizarEstadoSuscripcion(Long idUsuario) throws Exception;
}
