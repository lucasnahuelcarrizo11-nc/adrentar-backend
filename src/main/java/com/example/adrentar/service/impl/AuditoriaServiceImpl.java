package com.example.adrentar.service.impl;

import com.example.adrentar.dto.AuditoriaDto;
import com.example.adrentar.dto.ImagenAuditoriaDto;
import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.Auditoria;
import com.example.adrentar.entity.ImagenAuditoria;
import com.example.adrentar.entity.TipoAuditoria;
import com.example.adrentar.repository.AlquilerRepository;
import com.example.adrentar.repository.AuditoriaRepository;
import com.example.adrentar.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.adrentar.repository.ImagenAuditoriaRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuditoriaServiceImpl  implements AuditoriaService {
    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private ImagenAuditoriaRepository imagenAuditoriaRepository;

    @Autowired
    private AlquilerRepository alquilerRepository;

    private final String UPLOAD_DIR = "uploads/auditorias";

    /* ── Conversión a DTO (rompe el ciclo de serialización) ── */
    public AuditoriaDto toDto(Auditoria auditoria) {
        List<ImagenAuditoriaDto> imagenesDTO = auditoria.getImagenes().stream()
                .map(img -> new ImagenAuditoriaDto(img.getId(), img.getUrl(), img.getFechaCarga()))
                .collect(Collectors.toList());

        return new AuditoriaDto(
                auditoria.getId(),
                auditoria.getAlquiler().getIdAlquiler(),
                auditoria.getTipo().name(),
                auditoria.getFechaCreacion(),
                imagenesDTO
        );
    }

    @Override
    public Auditoria obtenerOCrearAuditoria(Long idAlquiler, TipoAuditoria tipo) {
        return auditoriaRepository.findByAlquiler_IdAlquilerAndTipo(idAlquiler, tipo)
                .orElseGet(() -> {
                    Alquiler alquiler = alquilerRepository.findById(idAlquiler)
                            .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));
                    Auditoria nueva = new Auditoria();
                    nueva.setAlquiler(alquiler);
                    nueva.setTipo(tipo);
                    nueva.setFechaCreacion(LocalDateTime.now());
                    return auditoriaRepository.save(nueva);
                });
    }

    @Override
    public Optional<Auditoria> buscar(Long idAlquiler, TipoAuditoria tipo) {
        return auditoriaRepository.findByAlquiler_IdAlquilerAndTipo(idAlquiler, tipo);
    }

    @Override
    public ImagenAuditoria guardarImagen(Long auditoriaId, MultipartFile archivo) throws IOException {
        Auditoria auditoria = auditoriaRepository.findById(auditoriaId)
                .orElseThrow(() -> new RuntimeException("Auditoria no encontrada"));

        String carpeta = UPLOAD_DIR + "/" + auditoriaId;
        File dir = new File(carpeta);
        if (!dir.exists()) dir.mkdirs();

        String nombreUnico = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        Path rutaArchivo = Paths.get(carpeta, nombreUnico);
        Files.write(rutaArchivo, archivo.getBytes());

        ImagenAuditoria imagen = new ImagenAuditoria();
        imagen.setAuditoria(auditoria);
        imagen.setNombreArchivo(nombreUnico);
        imagen.setUrl("/uploads/auditorias/" + auditoriaId + "/" + nombreUnico);
        imagen.setFechaCarga(LocalDateTime.now());

        return imagenAuditoriaRepository.save(imagen);
    }

    @Override
    public void eliminarImagen(Long imagenId) {
        imagenAuditoriaRepository.deleteById(imagenId);

    }
}
