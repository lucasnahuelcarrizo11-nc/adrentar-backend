package com.example.adrentar.dto;

import lombok.Data;

@Data
public class SendDocumentRequest {

    private String signerEmail;
    private String signerName;
    private String documentBase64;  // PDF en base64
    private String documentName;
}
