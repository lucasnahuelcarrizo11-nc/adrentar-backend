package com.example.adrentar.service.impl;

import com.example.adrentar.config.DocuSignConfig;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocuSignAuthService {

    private final DocuSignConfig config;

    private String accessToken;
    private Instant tokenExpiry;

    public String getAccessToken() throws Exception {
        if (accessToken == null || Instant.now().isAfter(tokenExpiry)) {
            refreshToken();
        }
        return accessToken;
    }

    /**
     * Obtiene el contenido "crudo" del archivo .pem (con headers, saltos de línea,
     * todo tal cual), sin importar si viene de una env var o de un archivo local.
     *
     * En producción (Render), DOCUSIGN_PRIVATE_KEY_B64 debe contener el archivo
     * .pem ENTERO codificado en Base64 (generado con `base64 -w0 archivo.key`).
     * Esto evita que Render corrompa guiones, saltos de línea o headers al
     * guardar la variable de entorno.
     *
     * Si esa variable no está seteada, se mantiene compatibilidad hacia atrás
     * con DOCUSIGN_PRIVATE_KEY (el PEM pegado directo) para no romper nada
     * si todavía la tenés configurada así en algún entorno.
     */
    private String loadRawPemContent() throws Exception {
        String envKeyB64 = System.getenv("DOCUSIGN_PRIVATE_KEY_B64");

        if (envKeyB64 != null && !envKeyB64.isBlank()) {
            byte[] decoded = Base64.getDecoder().decode(envKeyB64.trim());
            return new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
        }

        String envKeyRaw = System.getenv("DOCUSIGN_PRIVATE_KEY");
        if (envKeyRaw != null && !envKeyRaw.isBlank()) {
            System.out.println("=== ADVERTENCIA: usando DOCUSIGN_PRIVATE_KEY (formato legacy). "
                    + "Se recomienda migrar a DOCUSIGN_PRIVATE_KEY_B64 para evitar corrupción. ===");
            return envKeyRaw.replace("\\n", "\n");
        }

        // Local: viene del archivo
        Resource resource = new ClassPathResource("docusign_private.key");
        return new String(resource.getInputStream().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
    }

    private void refreshToken() throws Exception {
        String rawPem = loadRawPemContent();

        String keyContent = rawPem
                .replaceAll("-----BEGIN.*?-----", "")
                .replaceAll("-----END.*?-----", "")
                // Nos quedamos SOLO con caracteres válidos de base64, por las dudas
                // (saltos de línea, espacios u otros residuales).
                .replaceAll("[^A-Za-z0-9+/=]", "");

        System.out.println("=== keyContent length: " + keyContent.length() + " ===");

        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(keyContent);
        } catch (IllegalArgumentException ex) {
            System.out.println("=== keyContent inválido tras sanitizar. Preview: "
                    + keyContent.substring(0, Math.min(60, keyContent.length())) + "... ===");
            throw ex;
        }

        PrivateKey privateKey;
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            try {
                org.bouncycastle.asn1.pkcs.RSAPrivateKey rsaKey =
                        org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(keyBytes);
                RSAPrivateCrtKeySpec spec = new RSAPrivateCrtKeySpec(
                        rsaKey.getModulus(), rsaKey.getPublicExponent(),
                        rsaKey.getPrivateExponent(), rsaKey.getPrime1(),
                        rsaKey.getPrime2(), rsaKey.getExponent1(),
                        rsaKey.getExponent2(), rsaKey.getCoefficient()
                );
                privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
            } catch (Exception e2) {
                System.out.println("=== No se pudo parsear la clave privada ni como PKCS8 ni como PKCS1. "
                        + "keyContent length: " + keyContent.length() + " ===");
                throw e2;
            }
        }

        // 2. Construir el JWT
        long now = Instant.now().getEpochSecond();
        String jwt = Jwts.builder()
                .issuer(config.getIntegrationKey())
                .subject(config.getUserId())
                .audience().add(config.getOauthBasePath()).and()
                .issuedAt(new Date(now * 1000))
                .expiration(new Date((now + 3600) * 1000))
                .claim("scope", "signature impersonation")
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        System.out.println("=== DocuSign JWT generado OK ===");

        // 3. Intercambiar JWT por access token
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
        body.add("assertion", jwt);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        String tokenUrl = "https://" + config.getOauthBasePath() + "/oauth/token";

        System.out.println("=== Solicitando token a: " + tokenUrl + " ===");

        try {
            Map<String, Object> response = restTemplate.postForObject(tokenUrl, request, Map.class);
            System.out.println("=== Token response OK ===");

            accessToken = (String) response.get("access_token");
            Integer expiresIn = (Integer) response.get("expires_in");
            tokenExpiry = Instant.now().plusSeconds(expiresIn - 60);

            System.out.println("=== Access token obtenido OK ===");
        } catch (HttpClientErrorException e) {
            System.out.println("=== Error al obtener token: " + e.getResponseBodyAsString() + " ===");
            throw e;
        }
    }
}