package com.example.adrentar.controller;


import com.example.adrentar.dto.CrearAlquilerDto;
import com.example.adrentar.service.impl.AlquilerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerServiceImpl alquilerService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearAlquiler(
            @RequestHeader("Authorization") String token,
            @RequestBody CrearAlquilerDto dto) {

        alquilerService.crearAlquiler(token, dto);
        return ResponseEntity.ok("Alquiler creado correctamente");
    }

    @GetMapping("/mis-alquileres")
    public ResponseEntity<?> obtenerMisAlquileres(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                alquilerService.obtenerMisAlquileres(token)
        );
    }

    @PutMapping("/{id}/aceptar")
    public ResponseEntity<?> aceptar(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        alquilerService.aceptarAlquiler(token, id);
        return ResponseEntity.ok("Alquiler aceptado");
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        alquilerService.rechazarAlquiler(token, id);
        return ResponseEntity.ok("Alquiler rechazado");
    }
}
