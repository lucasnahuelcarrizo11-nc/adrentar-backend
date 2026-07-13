package com.example.adrentar.controller;

import com.example.adrentar.dto.AuditoriaDto;
import com.example.adrentar.dto.ImagenAuditoriaDto;
import com.example.adrentar.entity.Auditoria;
import com.example.adrentar.entity.ImagenAuditoria;
import com.example.adrentar.entity.TipoAuditoria;
import com.example.adrentar.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;


    @PostMapping("/alquiler/{alquilerId}")
    public ResponseEntity<AuditoriaDto> obtenerOCrear(
            @PathVariable Long alquilerId,
            @RequestParam TipoAuditoria tipo) {
        Auditoria auditoria = auditoriaService.obtenerOCrearAuditoria(alquilerId, tipo);
        return ResponseEntity.ok(auditoriaService.toDto(auditoria));
    }

    @GetMapping("/alquiler/{alquilerId}")
    public ResponseEntity<AuditoriaDto> obtener(
            @PathVariable Long alquilerId,
            @RequestParam TipoAuditoria tipo) {
        return auditoriaService.buscar(alquilerId, tipo)
                .map(auditoriaService::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{auditoriaId}/imagenes")
    public ResponseEntity<?> subirImagenes(
            @PathVariable Long auditoriaId,
            @RequestParam("archivos") List<MultipartFile> archivos) {
        try {
            List<ImagenAuditoriaDto> guardadas = new ArrayList<>();
            for (MultipartFile archivo : archivos) {
                ImagenAuditoria img = auditoriaService.guardarImagen(auditoriaId, archivo);
                guardadas.add(new ImagenAuditoriaDto(img.getId(), img.getUrl(), img.getFechaCarga()));
            }
            return ResponseEntity.ok(guardadas);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al subir imagen: " + e.getMessage());
        }
    }

    @DeleteMapping("/imagenes/{imagenId}")
    public ResponseEntity<?> eliminarImagen(@PathVariable Long imagenId) {
        auditoriaService.eliminarImagen(imagenId);
        return ResponseEntity.noContent().build();
    }
}