package com.example.adrentar.service.impl;

import com.example.adrentar.config.DocuSignConfig;
import com.example.adrentar.service.DocuSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocuSignServiceImpl implements DocuSignService {

    private final DocuSignAuthService authService;
    private final DocuSignConfig config;

    private HttpHeaders authHeaders() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String baseUrl() {
        return config.getBasePath() + "/v2.1/accounts/" + config.getAccountId();
    }

    @Override
    public String sendEnvelopeForTwoSigners(
            String signer1Email, String signer1Name,   // propietario (firma embebida)
            String signer2Email, String signer2Name,   // inquilino (firma por email)
            String documentBase64, String documentName) throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> document = Map.of(
                "documentBase64", documentBase64,
                "name", documentName,
                "fileExtension", "pdf",
                "documentId", "1"
        );

        Map<String, Object> signHere1 = Map.of(
                "anchorString", "/firma-propietario/",
                "anchorUnits", "pixels",
                "anchorXOffset", "20",
                "anchorYOffset", "-10"
        );

        Map<String, Object> signHere2 = Map.of(
                "anchorString", "/firma-inquilino/",
                "anchorUnits", "pixels",
                "anchorXOffset", "20",
                "anchorYOffset", "-10"
        );

        // Propietario: firma embebida (tiene clientUserId)
        Map<String, Object> propietario = Map.of(
                "email", signer1Email,
                "name", signer1Name,
                "recipientId", "1",
                "routingOrder", "1",
                "clientUserId", "1001",
                "tabs", Map.of("signHereTabs", List.of(signHere1))
        );

        // Inquilino: firma por email (sin clientUserId)
        Map<String, Object> inquilino = Map.of(
                "email", signer2Email,
                "name", signer2Name,
                "recipientId", "2",
                "routingOrder", "2",
                "tabs", Map.of("signHereTabs", List.of(signHere2))
        );

        Map<String, Object> envelope = Map.of(
                "emailSubject", "Contrato de alquiler - " + documentName.substring(0, Math.min(documentName.length(), 70)),
                "documents", List.of(document),
                "recipients", Map.of("signers", List.of(propietario, inquilino)),
                "status", "sent"
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(envelope, authHeaders());
        Map<String, Object> response = restTemplate.postForObject(
                baseUrl() + "/envelopes", request, Map.class
        );

        return (String) response.get("envelopeId");
    }

    @Override
    public String getEmbeddedSigningUrl(String envelopeId, String signerEmail,
                                        String signerName, String returnUrl) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = Map.of(
                "returnUrl", returnUrl,
                "authenticationMethod", "none",
                "email", signerEmail,
                "userName", signerName,
                "clientUserId", "1001"
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, authHeaders());
        Map<String, Object> response = restTemplate.postForObject(
                baseUrl() + "/envelopes/" + envelopeId + "/views/recipient",
                request, Map.class
        );

        return (String) response.get("url");
    }

    @Override
    public byte[] downloadSignedDocument(String envelopeId) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setAccept(List.of(MediaType.APPLICATION_PDF));

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                baseUrl() + "/envelopes/" + envelopeId + "/documents/combined",
                HttpMethod.GET, request, byte[].class
        );

        return response.getBody();
    }
}