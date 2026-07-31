package com.example.adrentar.controller;

import com.example.adrentar.dto.GastoPropiedadDto;
import com.example.adrentar.entity.Reparacion;
import com.example.adrentar.service.ReparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/reparaciones")
public class ReparacionController {

    private static final String CARPETA_REPARACIONES = "uploads/reparaciones";

    @Autowired
    private ReparacionService reparacionService;

    // CU-0017 / CP-044: registrar una reparación con descripción + imágenes + monto + propiedad
    @PostMapping
    public ResponseEntity<?> crearReparacion(
            @RequestParam("idProveedor") Long idProveedor,
            @RequestParam("idPropiedad") Long idPropiedad,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "monto", required = false) Double monto,
            @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes) {

        try {
            Reparacion guardada = reparacionService.crearReparacion(
                    idProveedor, idPropiedad, titulo, descripcion, monto, imagenes);
            return ResponseEntity.ok(guardada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("No se pudieron guardar las imágenes");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // CU-0017: listar las reparaciones del proveedor logueado
    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<List<Reparacion>> listarPorProveedor(@PathVariable Long idProveedor) {
        return ResponseEntity.ok(reparacionService.listarPorProveedor(idProveedor));
    }

    // Nuevo: resumen de gastos por propiedad para el propietario logueado
    @GetMapping("/gastos/resumen")
    public ResponseEntity<List<GastoPropiedadDto>> resumenGastos(
            @RequestParam Long idPropietario,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) Integer mes) {
        return ResponseEntity.ok(reparacionService.resumenGastos(idPropietario, anio, mes));
    }

    // Sirve cada imagen individual guardada
    @GetMapping("/imagenes/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreArchivo) {
        try {
            Path ruta = Paths.get(CARPETA_REPARACIONES).resolve(nombreArchivo);
            Resource recurso = new UrlResource(ruta.toUri());

            if (!recurso.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + nombreArchivo + "\"")
                    .body(recurso);
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}