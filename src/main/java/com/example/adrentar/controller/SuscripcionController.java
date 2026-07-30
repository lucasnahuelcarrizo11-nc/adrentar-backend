package com.example.adrentar.controller;

import com.example.adrentar.service.SuscripcionService;
import com.mercadopago.client.preapproval.PreapprovalClient;
import com.mercadopago.resources.preapproval.Preapproval;
import jakarta.servlet.http.HttpServletRequest;
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

            suscripcionService.sincronizarEstadoSuscripcion(idUsuario);

            return ResponseEntity.ok(
                    suscripcionService.obtenerEstado(idUsuario)
            );

        } catch (Exception e) {

            e.printStackTrace();

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
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody Map<String, Object> body) {

        System.out.println("================================");
        System.out.println("WEBHOOK RECIBIDO");
        System.out.println(body);
        System.out.println("================================");

        try {

            String type = body.get("type") == null ? "" : body.get("type").toString();
            String action = body.get("action") == null ? "" : body.get("action").toString();

            System.out.println("TYPE = " + type);
            System.out.println("ACTION = " + action);

            Map<String, Object> data = (Map<String, Object>) body.get("data");

            if (data != null) {

                String id = data.get("id").toString();

                System.out.println("RESOURCE ID = " + id);

                if (type.contains("preapproval") || type.contains("subscription")) {

                    System.out.println("Procesando PREAPPROVAL");

                    suscripcionService.procesarWebhookPreapproval(id);

                } else {

                    System.out.println("Webhook ignorado.");

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return ResponseEntity.ok().build();
    }


    @GetMapping("/webhook")
    public ResponseEntity<String> webhookGet(HttpServletRequest request) {

        System.out.println("================================");
        System.out.println("GET WEBHOOK");
        System.out.println(request.getQueryString());
        System.out.println("================================");

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/debug/{id}")
    public ResponseEntity<String> debug(@PathVariable String id) throws Exception {

        PreapprovalClient client = new PreapprovalClient();

        Preapproval p = client.get(id);

        return ResponseEntity.ok(
                "ID: " + p.getId()
                        + "\nSTATUS: " + p.getStatus()
                        + "\nEXTERNAL: " + p.getExternalReference()
        );
    }

    }
