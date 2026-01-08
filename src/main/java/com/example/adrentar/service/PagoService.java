package com.example.adrentar.service;

import com.example.adrentar.dto.PagoDto;
import com.example.adrentar.entity.Pago;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import java.util.List;

public interface PagoService {
    String crearPreference(Long idAlquiler, int mes, int anio) throws Exception;

    void procesarPagoDesdeWebhook(String paymentId);

    List<PagoDto> obtenerPagosPorAlquiler(Long idAlquiler);

}
