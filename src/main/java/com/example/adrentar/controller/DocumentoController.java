package com.example.adrentar.controller;

import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.entity.Documento;
import com.example.adrentar.entity.Usuario;
import com.example.adrentar.repository.AlquilerRepository;
import com.example.adrentar.service.DocumentoService;
import com.example.adrentar.service.impl.DocumentoServiceImpl;
import com.example.adrentar.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {
    @Autowired
    private DocumentoServiceImpl documentoService;

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    // 📤 Subir documento
    @PostMapping("/subir/{idAlquiler}")
    public ResponseEntity<?> subirDocumento(
            @RequestHeader("Authorization") String token,
            @PathVariable Long idAlquiler,
            @RequestParam("archivo") MultipartFile archivo) {

        try {
            String tokenLimpio = token.replace("Bearer ", "").trim();

            Usuario usuario = usuarioService.getUsuarioPorToken(tokenLimpio)
                    .orElseThrow(() -> new RuntimeException("Token inválido"));

            Alquiler alquiler = alquilerRepository.findById(idAlquiler)
                    .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));

            documentoService.guardar(archivo, alquiler, usuario);

            return ResponseEntity.ok("Documento subido correctamente");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 📄 Listar documentos por alquiler
    @GetMapping("/alquiler/{idAlquiler}")
    public ResponseEntity<?> listarDocumentos(
            @RequestHeader("Authorization") String token,
            @PathVariable Long idAlquiler) {

        String tokenLimpio = token.replace("Bearer ", "").trim();

        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorToken(tokenLimpio);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido");
        }

        return ResponseEntity.ok(
                documentoService.listarPorAlquiler(idAlquiler)
        );
    }

    // 🗑 Eliminar documento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDocumento(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        try {
            String tokenLimpio = token.replace("Bearer ", "").trim();

            Usuario usuario = usuarioService.getUsuarioPorToken(tokenLimpio)
                    .orElseThrow(() -> new RuntimeException("Token inválido"));

            documentoService.eliminar(id);

            return ResponseEntity.ok("Documento eliminado");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/descargar/{idDocumento}")
    public ResponseEntity<?> descargarDocumento(
            @RequestHeader("Authorization") String token,
            @PathVariable Long idDocumento) {

        try {
            String tokenLimpio = token.replace("Bearer ", "").trim();
            usuarioService.getUsuarioPorToken(tokenLimpio)
                    .orElseThrow(() -> new RuntimeException("Token inválido"));

            Documento doc = documentoService.buscarPorId(idDocumento)
                    .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

            Path path = Paths.get(doc.getRutaArchivo());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "inline; filename=\"" + doc.getNombreArchivo() + "\"")
                    .header("Content-Type", doc.getTipoArchivo())
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
