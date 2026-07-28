package com.example.adrentar.controller;

import com.example.adrentar.service.SuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/suscripciones")
@RequiredArgsConstructor
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    @GetMapping("/estado/{idUsuario}")
    public ResponseEntity<?> obtenerEstado(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(suscripcionService.obtenerEstado(idUsuario));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearSuscripcion(@RequestParam Long idUsuario) {
        try {
            String initPoint = suscripcionService.crearSuscripcionMP(idUsuario);
            Map<String, String> response = new HashMap<>();
            response.put("initPoint", initPoint);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/cancelar")
    public ResponseEntity<?> cancelarSuscripcion(@RequestParam Long idUsuario) {
        try {
            suscripcionService.cancelarSuscripcion(idUsuario);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Configurar esta URL en el dashboard de MercadoPago:
    // Developers > Tu aplicación > Webhooks > tópico "Suscripciones" (preapproval)
    // https://adrentar-backend.onrender.com/api/suscripciones/webhook
    @SuppressWarnings("unchecked")
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody Map<String, Object> body) {
        System.out.println("Webhook suscripción recibido: " + body);
        try {
            String type = (String) body.get("type");
            String preapprovalId = null;

            if (body.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                if (data.get("id") != null) {
                    preapprovalId = data.get("id").toString();
                }
            }

            if (preapprovalId != null) {
                suscripcionService.procesarWebhookPreapproval(preapprovalId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }
}