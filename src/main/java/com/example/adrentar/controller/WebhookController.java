package com.example.adrentar.controller;


import com.example.adrentar.service.PagoService;
import com.example.adrentar.service.impl.PagoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/mercadopago")
public class WebhookController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    public ResponseEntity<Void> recibirWebhook(@RequestBody Map<String, Object> payload) {

        if (!payload.containsKey("data")) {
            return ResponseEntity.ok().build();
        }

        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        String paymentId = data.get("id").toString();

        pagoService.procesarPagoDesdeWebhook(paymentId);

        return ResponseEntity.ok().build();
    }
}

