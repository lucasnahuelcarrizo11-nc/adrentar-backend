package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.Documento;
import com.example.adrentar.entity.Usuario;
import com.example.adrentar.repository.DocumentoRepository;
import com.example.adrentar.service.DocumentoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentoServiceImpl implements DocumentoService {

    @Value("${documentos.path}")
    private String rutaBase;

    @Autowired
    private DocumentoRepository repo;


    @Override
    public Documento guardar(MultipartFile archivo, Alquiler alquiler, Usuario usuario) throws IOException {

        Files.createDirectories(Paths.get(rutaBase));

        String nombre = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        Path destino = Paths.get(rutaBase, nombre);

        Files.copy(archivo.getInputStream(), destino);

        Documento doc = new Documento();
        doc.setAlquiler(alquiler);
        doc.setUsuario(usuario);
        doc.setNombreArchivo(archivo.getOriginalFilename());
        doc.setTipoArchivo(archivo.getContentType());
        doc.setRutaArchivo(destino.toString());
        doc.setFechaCarga(LocalDateTime.now());

        return repo.save(doc);
    }
    @Override
    public List<Documento> listarPorAlquiler(Long idAlquiler) {
        return repo.findByAlquiler_IdAlquiler(idAlquiler);
    }

    @Override
    public void eliminar(Long id) throws IOException {
        Documento doc = repo.findById(id).orElseThrow();
        Files.deleteIfExists(Paths.get(doc.getRutaArchivo()));
        repo.delete(doc);
    }

    @Override
    public Optional<Documento> buscarPorId(Long id) {
        return repo.findById(id);
    }
}
