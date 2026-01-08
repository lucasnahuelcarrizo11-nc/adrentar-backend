package com.example.adrentar.controller;

import com.example.adrentar.entity.Inquilino;
import com.example.adrentar.entity.Propietario;
import com.example.adrentar.service.InquilinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin( origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/inquilinos")
public class InquilinoController {

    @Autowired
    private InquilinoService inquilinoService;

    @PostMapping
    public ResponseEntity<?> registrarInquilino(@RequestBody Inquilino inquilino)  {
        Inquilino nuevoInquilino = inquilinoService.crearInquilino(inquilino);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInquilino);
    }

    @GetMapping
    public ResponseEntity <List<Inquilino>> listarInquilinos() {
        List <Inquilino> inquilino = inquilinoService.listarInquilinos();
        return ResponseEntity.ok(inquilino);
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity <?> buscarPorNombre(@PathVariable String nombre) {
        Optional<Inquilino> inquilino = inquilinoService.buscarInquilinoPorNombre(nombre);
        return inquilino.isPresent() ? ResponseEntity.ok(inquilino.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquilino no encontrado");
    }

    @GetMapping("/buscar/id/{idInquilino}")
    public ResponseEntity <?> buscarPorid(@PathVariable Long idInquilino ) {
        Optional<Inquilino> inquilino = inquilinoService.buscarInquilinoPorId(idInquilino);
        return inquilino.isPresent() ? ResponseEntity.ok(inquilino.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquilino no encontrado");
    }

    @GetMapping("/buscar/email/{email:.+}")
    public ResponseEntity <?> buscarPorEmail(@PathVariable String email) {
        Optional <Inquilino> inquilino = inquilinoService.buscarInquilinoPorEmail(email);
        return   inquilino.isPresent() ? ResponseEntity.ok(inquilino.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquilino no encontrado");
    }
    @PutMapping("/actualizar/{idInquilino}")
    public ResponseEntity<?> actualizarInquilino(@PathVariable Long idInquilino, @RequestBody Inquilino inquilino ) {
        try {
            Inquilino actualizado = inquilinoService.actualizarInquilino(idInquilino, inquilino);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/{idInquilino}")
    public ResponseEntity <?> eliminarPropietario(@PathVariable Long idInquilino) {
        try {
            inquilinoService.eliminarInquilino(idInquilino);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Inquilino eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquilino no encontrado");
        }
    }


}
