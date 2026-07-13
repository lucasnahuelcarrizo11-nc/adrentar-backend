package com.example.adrentar.controller;

import com.example.adrentar.dto.EmbeddedSignRequest;
import com.example.adrentar.dto.SendContratoRequest;
import com.example.adrentar.repository.AlquilerRepository;
import com.example.adrentar.service.DocuSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/docusign")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}")
public class DocuSignController {

    private final DocuSignService docuSignService;
    private final AlquilerRepository alquilerRepository;

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

            if (request.getIdAlquiler() != null) {
                alquilerRepository.findById(request.getIdAlquiler()).ifPresent(alq -> {
                    alq.setEnvelopeId(envelopeId);
                    alquilerRepository.save(alq);
                });
            }

            // Map.of() no acepta nulls — usar HashMap en su lugar
            Map<String, String> response = new HashMap<>();
            response.put("envelopeId", envelopeId != null ? envelopeId : "");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage() != null ? e.getMessage() : "Error desconocido");
            return ResponseEntity.status(500).body(error);
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

    @GetMapping("/envelope/{idAlquiler}")
    public ResponseEntity<Map<String, String>> getEnvelopeId(@PathVariable Long idAlquiler) {
        return alquilerRepository.findById(idAlquiler)
                .filter(alq -> alq.getEnvelopeId() != null)
                .map(alq -> ResponseEntity.ok(Map.of("envelopeId", alq.getEnvelopeId())))
                .orElse(ResponseEntity.notFound().build());
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

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload) {
        System.out.println("DocuSign webhook: " + payload);
        return ResponseEntity.ok().build();
    }
}