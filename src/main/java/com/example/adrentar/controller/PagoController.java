package com.example.adrentar.controller;

import com.example.adrentar.entity.Pago;
import com.example.adrentar.service.PagoService;
import com.example.adrentar.service.impl.PagoServiceImpl;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "http://localhost:5173")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping("/preference")
    public ResponseEntity<?> crearPago(
            @RequestParam Long idAlquiler,
            @RequestParam int mes,
            @RequestParam int anio) {
        try {
            return ResponseEntity.ok(
                    pagoService.crearPreference(idAlquiler, mes, anio)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/alquiler/{idAlquiler}")
    public ResponseEntity<?> pagosPorAlquiler(@PathVariable Long idAlquiler) {
        return ResponseEntity.ok(
                pagoService.obtenerPagosPorAlquiler(idAlquiler)
        );
    }
}
