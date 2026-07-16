package com.example.adrentar.controller;

import com.example.adrentar.entity.Resena;
import com.example.adrentar.service.impl.ResenaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaServiceImpl resenaServiceImpl;

    @PostMapping("/crear")
    public ResponseEntity<?> crearResena(@RequestBody Resena resena) {
        try {
            Resena nueva = resenaServiceImpl.crearResena(resena);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<List<Resena>> listarPorProveedor(@PathVariable Long idProveedor) {
        return ResponseEntity.ok(resenaServiceImpl.listarPorProveedor(idProveedor));
    }

    @DeleteMapping("/{idResena}")
    public ResponseEntity<?> eliminarResena(@PathVariable Long idResena) {
        try {
            resenaServiceImpl.eliminarResena(idResena);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Reseña eliminada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reseña no encontrada");
        }
    }
}
