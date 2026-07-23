package com.example.adrentar.controller;

import com.example.adrentar.dto.ProveedorConPromedioDTO;
import com.example.adrentar.entity.Propietario;
import com.example.adrentar.entity.Proveedor;
import com.example.adrentar.service.ProveedorService;
import com.example.adrentar.service.impl.ProveedorServiceImpl;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorServiceImpl proveedorServiceImpl;

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


}

