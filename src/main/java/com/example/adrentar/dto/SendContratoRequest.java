package com.example.adrentar.dto;

import lombok.Data;

@Data
public class SendContratoRequest {
    private String propietarioEmail;
    private String propietarioNombre;
    private String inquilinoEmail;
    private String inquilinoNombre;
    private String documentBase64;
    private String documentName;
}
