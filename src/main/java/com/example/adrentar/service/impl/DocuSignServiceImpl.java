package com.example.adrentar.service.impl;

import com.example.adrentar.config.DocuSignConfig;
import com.example.adrentar.service.DocuSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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
            String signer1Email, String signer1Name,
            String signer2Email, String signer2Name,
            String documentBase64, String documentName) throws Exception {

        // Log para detectar qué campo viene null
        System.out.println("=== sendEnvelopeForTwoSigners ===");
        System.out.println("signer1Email: " + signer1Email);
        System.out.println("signer1Name: " + signer1Name);
        System.out.println("signer2Email: " + signer2Email);
        System.out.println("signer2Name: " + signer2Name);
        System.out.println("documentName: " + documentName);
        System.out.println("documentBase64 null?: " + (documentBase64 == null));

        // Validar que ningún campo sea null
        if (signer1Email == null || signer1Name == null ||
                signer2Email == null || signer2Name == null ||
                documentBase64 == null || documentName == null) {
            throw new Exception("Faltan datos obligatorios para crear el envelope. Revisá los logs.");
        }

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> document = new HashMap<>();
        document.put("documentBase64", documentBase64);
        document.put("name", documentName);
        document.put("fileExtension", "pdf");
        document.put("documentId", "1");

        Map<String, Object> signHere1 = new HashMap<>();
        signHere1.put("anchorString", "/firma-propietario/");
        signHere1.put("anchorUnits", "pixels");
        signHere1.put("anchorXOffset", "20");
        signHere1.put("anchorYOffset", "-10");

        Map<String, Object> signHere2 = new HashMap<>();
        signHere2.put("anchorString", "/firma-inquilino/");
        signHere2.put("anchorUnits", "pixels");
        signHere2.put("anchorXOffset", "20");
        signHere2.put("anchorYOffset", "-10");

        Map<String, Object> propietario = new HashMap<>();
        propietario.put("email", signer1Email);
        propietario.put("name", signer1Name);
        propietario.put("recipientId", "1");
        propietario.put("routingOrder", "1");
        propietario.put("clientUserId", "1001");
        propietario.put("tabs", Map.of("signHereTabs", List.of(signHere1)));

        Map<String, Object> inquilino = new HashMap<>();
        inquilino.put("email", signer2Email);
        inquilino.put("name", signer2Name);
        inquilino.put("recipientId", "2");
        inquilino.put("routingOrder", "2");
        inquilino.put("tabs", Map.of("signHereTabs", List.of(signHere2)));

        Map<String, Object> envelope = new HashMap<>();
        envelope.put("emailSubject", "Contrato de alquiler - " + documentName.substring(0, Math.min(documentName.length(), 70)));
        envelope.put("documents", List.of(document));
        envelope.put("recipients", Map.of("signers", List.of(propietario, inquilino)));
        envelope.put("status", "sent");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(envelope, authHeaders());

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    baseUrl() + "/envelopes", request, Map.class
            );
            System.out.println("=== DocuSign response: " + response + " ===");
            return (String) response.get("envelopeId");
        } catch (HttpClientErrorException e) {
            System.out.println("=== Error DocuSign: " + e.getResponseBodyAsString() + " ===");
            throw new Exception("Error DocuSign: " + e.getResponseBodyAsString());
        }
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