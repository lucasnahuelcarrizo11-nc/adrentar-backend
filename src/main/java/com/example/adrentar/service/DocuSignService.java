package com.example.adrentar.service;

public interface DocuSignService {

    public String sendEnvelopeByEmail(String signerEmail, String signerName, String documentBase64, String documentName) throws Exception;
    public String getEmbeddedSigningUrl(String envelopeId, String signerEmail, String signerName, String returnUrl) throws Exception;
    public byte[] downloadSignedDocument(String envelopeId) throws Exception;

}
