package com.example.adrentar.service;

public interface DocuSignService {

     String sendEnvelopeForTwoSigners(
            String signer1Email, String signer1Name,   // propietario (firma embebida)
            String signer2Email, String signer2Name,   // inquilino (firma por email)
            String documentBase64, String documentName) throws Exception;
     String getEmbeddedSigningUrl(String envelopeId, String signerEmail, String signerName, String returnUrl) throws Exception;
     byte[] downloadSignedDocument(String envelopeId) throws Exception;

}
