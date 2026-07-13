package com.example.adrentar.service;

import com.example.adrentar.entity.Alquiler;

import java.io.IOException;


public interface ContratoPdfService {

    byte[] generarContratoPdf(Alquiler alquiler) throws IOException;

    String sanitizar(String texto);

}
