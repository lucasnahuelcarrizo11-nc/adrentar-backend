package com.example.adrentar.controller;

import com.example.adrentar.dto.EmbeddedSignRequest;
import com.example.adrentar.dto.SendContratoRequest;
import com.example.adrentar.dto.SendDocumentRequest;
import com.example.adrentar.service.DocuSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/docusign")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}")
public class DocuSignController {
    private final DocuSignService docuSignService;

    @PostMapping("/send-contrato")
    public ResponseEntity<Map<String, String>> sendContrato(
            @RequestBody SendContratoRequest request) {
        try {
            String envelopeId = docuSignService.sendEnvelopeForTwoSigners(
                    request.getPropietarioEmail(),
                    request.getPropietarioNombre(),
                    request.getInquilinoEmail(),
                    request.getInquilinoNombre(),
                    request.getDocumentBase64(),
                    request.getDocumentName()
            );
            return ResponseEntity.ok(Map.of("envelopeId", envelopeId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/embedded-url")
    public ResponseEntity<Map<String, String>> getEmbeddedUrl(
            @RequestBody EmbeddedSignRequest request) {
        try {
            String url = docuSignService.getEmbeddedSigningUrl(
                    request.getEnvelopeId(),
                    request.getSignerEmail(),
                    request.getSignerName(),
                    request.getReturnUrl()
            );
            return ResponseEntity.ok(Map.of("signingUrl", url));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/download/{envelopeId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String envelopeId) {
        try {
            byte[] doc = docuSignService.downloadSignedDocument(envelopeId);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=documento-firmado.pdf")
                    .body(doc);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Webhook de DocuSign para recibir notificaciones de estado
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload) {
        // Parsear el XML/JSON de DocuSign y actualizar tu DB
        // Eventos: envelope-completed, envelope-sent, envelope-declined, etc.
        System.out.println("DocuSign webhook: " + payload);
        return ResponseEntity.ok().build();

    }


    }
