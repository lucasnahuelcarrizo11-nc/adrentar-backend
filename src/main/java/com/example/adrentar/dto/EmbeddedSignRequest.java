package com.example.adrentar.dto;

import lombok.Data;

@Data
public class EmbeddedSignRequest {
    private String envelopeId;
    private String signerEmail;
    private String signerName;
    private String returnUrl;  // URL
}
