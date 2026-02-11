package com.example.adrentar.service;

import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.Documento;
import com.example.adrentar.entity.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DocumentoService {


    public Documento guardar(MultipartFile archivo, Alquiler alquiler, Usuario usuario) throws IOException;

    List<Documento> listarPorAlquiler(Long idAlquiler);

    void eliminar(Long id) throws IOException;

    Optional<Documento> buscarPorId(Long id);
}
