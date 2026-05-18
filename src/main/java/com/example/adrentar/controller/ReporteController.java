package com.example.adrentar.controller;

import com.example.adrentar.dto.ReporteDto;
import com.example.adrentar.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    @Autowired
    private ReporteService reporteService;

    @PostMapping
    public ResponseEntity<?> crearReporte(@RequestBody ReporteDto dto) {
        try {
            return ResponseEntity.ok(reporteService.crearReporte(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/alquiler/{idAlquiler}")
    public ResponseEntity<?> obtenerPorAlquiler(@PathVariable Long idAlquiler) {
        try {
            return ResponseEntity.ok(reporteService.obtenerPorAlquiler(idAlquiler));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{idReporte}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long idReporte,
            @RequestParam String estado) {
        try {
            return ResponseEntity.ok(reporteService.cambiarEstado(idReporte, estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
