package com.example.adrentar.controller;

import com.example.adrentar.entity.Pago;
import com.example.adrentar.service.PagoService;
import com.example.adrentar.service.impl.PagoServiceImpl;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping("/preference")
    public ResponseEntity<?> crearPago(
            @RequestParam Long idAlquiler,
            @RequestParam int mes,
            @RequestParam int anio) {
        try {
            return ResponseEntity.ok(
                    pagoService.crearPreference(idAlquiler, mes, anio)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    e.getClass().getSimpleName() + " - " + e.getMessage()
            );
    }
}
    @GetMapping("/alquiler/{idAlquiler}")
    public ResponseEntity<?> pagosPorAlquiler(@PathVariable Long idAlquiler) {
        return ResponseEntity.ok(
                pagoService.obtenerPagosPorAlquiler(idAlquiler)
        );
    }


    @PostMapping("/webhook")
    public ResponseEntity<?> recibirWebhook(@RequestBody Map<String, Object> body) {

        System.out.println("Webhook recibido: " + body);

        try {

            Map<String, Object> data = (Map<String, Object>) body.get("data");

            if (data != null && data.get("id") != null) {

                Long paymentId = Long.valueOf(data.get("id").toString());

                pagoService.procesarPago(paymentId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }
}
