package com.example.adrentar.controller;

import com.example.adrentar.dto.ResumenInquilinoDto;
import com.example.adrentar.service.InquilinoResumenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquilino")
public class InquilinoResumenController {

    @Autowired
    private InquilinoResumenService inquilinoResumenService;

    @GetMapping("/mi-alquiler/{idAlquiler}/resumen")
    public ResponseEntity<?> resumen(
            @PathVariable Long idAlquiler,
            @RequestParam Long idInquilino) {
        try {
            return ResponseEntity.ok(inquilinoResumenService.obtenerResumen(idAlquiler, idInquilino));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}