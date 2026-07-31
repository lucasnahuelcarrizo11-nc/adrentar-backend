package com.example.adrentar.controller;

import com.example.adrentar.dto.IngresoMensualDto;
import com.example.adrentar.dto.OperacionDto;
import com.example.adrentar.service.OperacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operaciones")
public class OperacionesController {

    @Autowired
    private OperacionesService operacionesService;

    @GetMapping("/detalle")
    public ResponseEntity<List<OperacionDto>> detalle(@RequestParam Long idPropietario) {
        return ResponseEntity.ok(operacionesService.detalleOperaciones(idPropietario));
    }

    @GetMapping("/ingresos-mensuales")
    public ResponseEntity<List<IngresoMensualDto>> ingresosMensuales(
            @RequestParam Long idPropietario,
            @RequestParam Integer anio) {
        return ResponseEntity.ok(operacionesService.ingresosMensuales(idPropietario, anio));
    }
}