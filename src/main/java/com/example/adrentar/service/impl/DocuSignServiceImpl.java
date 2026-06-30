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
    public String sendEnvelopeByEmail(String signerEmail, String signerName,
                                      String documentBase64, String documentName) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> document = Map.of(
                "documentBase64", documentBase64,
                "name", documentName,
                "fileExtension", "pdf",
                "documentId", "1"
        );

        Map<String, Object> signHere = Map.of(
                "anchorString", "/firma/",
                "anchorUnits", "pixels",
                "anchorXOffset", "20",
                "anchorYOffset", "-10"
        );

        Map<String, Object> signer = Map.of(
                "email", signerEmail,
                "name", signerName,
                "recipientId", "1",
                "routingOrder", "1",
                "clientUserId", "1001",   // ← agregar esta línea
                "tabs", Map.of("signHereTabs", List.of(signHere))
        );

        Map<String, Object> envelope = Map.of(
                "emailSubject", "Por favor firmá el documento: " + documentName,
                "documents", List.of(document),
                "recipients", Map.of("signers", List.of(signer)),
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