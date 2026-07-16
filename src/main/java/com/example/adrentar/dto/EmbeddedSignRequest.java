package com.example.adrentar.dto;

import lombok.Data;

@Data
public class EmbeddedSignRequest {
    private Long idAlquiler;
    private String returnUrl;  // URL
}
