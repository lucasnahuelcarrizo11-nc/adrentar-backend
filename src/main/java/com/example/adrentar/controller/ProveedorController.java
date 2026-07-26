package com.example.adrentar.controller;

import com.example.adrentar.dto.ProveedorConPromedioDTO;
import com.example.adrentar.entity.Propietario;
import com.example.adrentar.entity.Proveedor;
import com.example.adrentar.repository.ProveedorRepository;
import com.example.adrentar.service.ProveedorService;
import com.example.adrentar.service.impl.ProveedorServiceImpl;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.multipart.MultipartFile;
 import org.springframework.core.io.Resource;
 import org.springframework.core.io.UrlResource;
 import java.io.IOException;
 import java.net.MalformedURLException;
 import java.nio.file.Files;
 import java.nio.file.Path;
 import java.nio.file.Paths;



import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorServiceImpl proveedorServiceImpl;

    @Autowired
    private ProveedorRepository proveedorRepository;

    private static final String CARPETA_MATRICULAS = "uploads/matriculas";

    @PostMapping("/crear")
    public ResponseEntity<?> crearProveedor(@RequestBody Proveedor proveedor) throws Exception {
        Proveedor nuevoProveedor = proveedorServiceImpl.crearProveedor(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProveedor);
    }

    @GetMapping("/listarProveedores")
    public ResponseEntity<List<ProveedorConPromedioDTO>> listarProveedores() {
        List<ProveedorConPromedioDTO> proveedores = proveedorServiceImpl.listarProveedoresConPromedio();
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/buscar/id/{idProveedor}")
    public ResponseEntity <?> buscarPorid(@PathVariable Long idProveedor ) {
        Optional<Proveedor> proveedor = proveedorServiceImpl.buscarPorId(idProveedor);
        return proveedor.isPresent() ? ResponseEntity.ok(proveedor.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proveedor no encontrado");
    }

    @PutMapping("/actualizar/{idProveedor}")
    public ResponseEntity<?> actualizarPropietario(
            @PathVariable Long idProveedor,
            @RequestBody Proveedor proveedor) {

        try {
            Proveedor actualizado = proveedorServiceImpl.actualizarProveedor(idProveedor, proveedor);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idProveedor}")
    public ResponseEntity <?> eliminarPropietario(@PathVariable Long idProveedor) {
        try {
            proveedorServiceImpl.eliminarProveedor(idProveedor);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Proveedor eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proveedor no encontrado");
        }
    }


    @PostMapping("/{id}/matricula")
    public ResponseEntity<?> subirMatricula(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        // Validaciones básicas
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }
        if (!"application/pdf".equals(file.getContentType())) {
            return ResponseEntity.badRequest().body("El archivo debe ser un PDF");
        }

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        try {
            Path carpetaDestino = Paths.get(CARPETA_MATRICULAS);
            Files.createDirectories(carpetaDestino);

            String nombreArchivo = "proveedor_" + id + "_matricula.pdf";
            Path rutaDestino = carpetaDestino.resolve(nombreArchivo);
            file.transferTo(rutaDestino);

            // URL pública desde donde se sirve el archivo (ver configuración de
            // recursos estáticos más abajo)
            proveedor.setMatriculaUrl("/api/proveedores/" + id + "/matricula");
            proveedorRepository.save(proveedor);

            return ResponseEntity.ok(proveedor);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("No se pudo guardar el archivo");
        }
    }

    @GetMapping("/{id}/matricula")
    public ResponseEntity<Resource> descargarMatricula(@PathVariable Long id) {
        try {
            Path ruta = Paths.get(CARPETA_MATRICULAS).resolve("proveedor_" + id + "_matricula.pdf");
            Resource recurso = new UrlResource(ruta.toUri());

            if (!recurso.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "inline; filename=\"matricula.pdf\"")
                    .body(recurso);
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}

