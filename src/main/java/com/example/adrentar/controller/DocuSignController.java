package com.example.adrentar.controller;

import com.example.adrentar.dto.EmbeddedSignRequest;
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

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendDocument(
            @RequestBody SendDocumentRequest request) {
        try {
            String envelopeId = docuSignService.sendEnvelopeByEmail(
                    request.getSignerEmail(),
                    request.getSignerName(),
                    request.getDocumentBase64(),
                    request.getDocumentName()
            );
            return ResponseEntity.ok(Map.of("envelopeId", envelopeId, "status", "sent"));
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

    @GetMapping("/test-send")
    public ResponseEntity<Map<String, String>> testSend() {
        try {

            String envelopeId = docuSignService.sendEnvelopeByEmail(
                    "lcAdrentar@yopmail.com",
                    "Lucas Test",
                    "pdfBase64",
                    "Contrato de prueba"
            );
            return ResponseEntity.ok(Map.of("envelopeId", envelopeId, "status", "sent"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}