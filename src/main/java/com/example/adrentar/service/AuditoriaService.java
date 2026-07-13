package com.example.adrentar.service;

import com.example.adrentar.dto.AuditoriaDto;
import com.example.adrentar.entity.Auditoria;
import com.example.adrentar.entity.ImagenAuditoria;
import com.example.adrentar.entity.TipoAuditoria;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface AuditoriaService {
     Auditoria obtenerOCrearAuditoria(Long alquilerId, TipoAuditoria tipo);

    AuditoriaDto toDto(Auditoria auditoria);
    Optional<Auditoria> buscar(Long alquilerId, TipoAuditoria tipo);

     ImagenAuditoria guardarImagen(Long auditoriaId, MultipartFile archivo) throws IOException;

    void eliminarImagen(Long imagenId);
}
