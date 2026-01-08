package com.example.adrentar.service.impl;


import com.example.adrentar.dto.PagoDto;
import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.EstadoPago;
import com.example.adrentar.entity.Pago;
import com.example.adrentar.repository.AlquilerRepository;
import com.example.adrentar.repository.PagoRepository;
import com.example.adrentar.service.PagoService;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.resources.preference.PreferenceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private AlquilerRepository alquilerRepository;

    public String crearPreference(Long idAlquiler, int mes, int anio) throws Exception {

        Alquiler alquiler = alquilerRepository.findById(idAlquiler)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));

        if (!alquiler.getEstado().equals("ACEPTADO")) {
            throw new RuntimeException("El alquiler no está aceptado");
        }

        if (pagoRepository.existsByAlquilerAndMesAndAnio(alquiler, mes, anio)) {
            throw new RuntimeException("Ese mes ya fue pagado");
        }

        BigDecimal monto = BigDecimal.valueOf(alquiler.getPrecio());

        String externalReference = idAlquiler + "-" + mes + "-" + anio; // 🔥 CLAVE

        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title("Alquiler " + mes + "/" + anio)
                .quantity(1)
                .unitPrice(monto)
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(List.of(item))
                .externalReference(externalReference)
                .build();

        Preference preference = new PreferenceClient().create(preferenceRequest);

        Pago pago = new Pago();
        pago.setAlquiler(alquiler);
        pago.setMes(mes);
        pago.setAnio(anio);
        pago.setMonto(alquiler.getPrecio());
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        pago.setPreferenceId(preference.getId());
        pago.setExternalReference(externalReference); // ✅ AGREGADO
        pago.setFechaCreacion(new Date());

        pagoRepository.save(pago);

        return preference.getInitPoint();
    }

    public List<PagoDto> obtenerPagosPorAlquiler(Long idAlquiler) {
        return pagoRepository.findByAlquilerIdAlquiler(idAlquiler)
                .stream()
                .map(p -> new PagoDto(
                        p.getMes(),
                        p.getAnio(),
                        p.getEstadoPago().name() // 🔥 CLAVE
                ))
                .toList();
    }


    @Override
    public void procesarPagoDesdeWebhook(String paymentId) {

        try {
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(Long.parseLong(paymentId));

            String externalReference = payment.getExternalReference(); // ✅ CLAVE
            String status = payment.getStatus(); // approved, rejected, pending

            Pago pago = pagoRepository.findByExternalReference(externalReference)
                    .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

            pago.setPaymentIdMP(paymentId);

            if ("approved".equalsIgnoreCase(status)) {
                pago.setEstadoPago(EstadoPago.APROBADO);
            } else if ("rejected".equalsIgnoreCase(status)) {
                pago.setEstadoPago(EstadoPago.RECHAZADO);
            } else {
                pago.setEstadoPago(EstadoPago.PENDIENTE);
            }

            pago.setFechaActualizacion(new Date());
            pagoRepository.save(pago);

        } catch (Exception e) {
            throw new RuntimeException("Error procesando webhook MP", e);
        }
    }
}
