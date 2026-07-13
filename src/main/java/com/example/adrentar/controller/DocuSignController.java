package com.example.adrentar.controller;

import com.example.adrentar.dto.EmbeddedSignRequest;
import com.example.adrentar.dto.SendContratoRequest;
import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.repository.AlquilerRepository;
import com.example.adrentar.service.DocuSignService;
import com.example.adrentar.service.impl.ContratoPdfServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/docusign")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}")
public class DocuSignController {

    private final DocuSignService docuSignService;
    private final AlquilerRepository alquilerRepository;
    private final ContratoPdfServiceImpl contratoPdfService;

    @PostMapping("/send-contrato")
    public ResponseEntity<Map<String, String>> sendContrato(
            @RequestBody SendContratoRequest request) {
        try {
            if (request.getIdAlquiler() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "idAlquiler es obligatorio");
                return ResponseEntity.badRequest().body(error);
            }

            Alquiler alquiler = alquilerRepository.findById(request.getIdAlquiler())
                    .orElseThrow(() -> new RuntimeException("Alquiler no encontrado: " + request.getIdAlquiler()));

            if (alquiler.getPropietario() == null || alquiler.getInquilino() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El alquiler no tiene propietario o inquilino asignado");
                return ResponseEntity.badRequest().body(error);
            }

            // Generar el PDF en el momento, con los datos reales del alquiler.
            // No depende de ningún archivo local ni de nada hardcodeado en el frontend.
            byte[] pdfBytes = contratoPdfService.generarContratoPdf(alquiler);
            String documentBase64 = Base64.getEncoder().encodeToString(pdfBytes);

            String direccion = alquiler.getPropiedad() != null ? alquiler.getPropiedad().getDireccion() : "Propiedad";
            String documentName = "Contrato - " + direccion;
            if (documentName.length() > 70) {
                documentName = documentName.substring(0, 70);
            }

            String envelopeId = docuSignService.sendEnvelopeForTwoSigners(
                    alquiler.getPropietario().getEmail(),
                    alquiler.getPropietario().getNombre(),
                    alquiler.getInquilino().getEmail(),
                    alquiler.getInquilino().getNombre(),
                    documentBase64,
                    documentName
            );

            alquiler.setEnvelopeId(envelopeId);
            alquilerRepository.save(alquiler);

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