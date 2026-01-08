package com.example.adrentar.controller;

import com.example.adrentar.entity.Propietario;
import com.example.adrentar.service.PropietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin( origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/propietarios")
public class PropietarioController {

    @Autowired
    private PropietarioService propietarioService;


    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPropietario(@RequestBody Propietario propietario )  {
        Propietario nuevoPropietario = propietarioService.crearPropietario(propietario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPropietario);
    }

    @GetMapping("/listarPropietarios")
    public ResponseEntity <List<Propietario>> listarPropietarios() {
        List <Propietario> propietario = propietarioService.mostrarPropietarios();
        return ResponseEntity.ok(propietario);
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity <?> buscarPorNombre(@PathVariable String nombre) {
        Optional<Propietario> propietario = propietarioService.buscarPorNombre(nombre);
        return propietario.isPresent() ? ResponseEntity.ok(propietario.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propietario no encontrado");
    }

    @GetMapping("/buscar/id/{idPropietario}")
    public ResponseEntity <?> buscarPorid(@PathVariable Long idPropietario ) {
        Optional<Propietario> propietario = propietarioService.buscarPorId(idPropietario);
        return propietario.isPresent() ? ResponseEntity.ok(propietario.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propietario no encontrado");
    }


    @PutMapping("/actualizar/{idPropietario}")
    public ResponseEntity<?> actualizarPropietario(
            @PathVariable Long idPropietario,
            @RequestBody Propietario propietario) {

        try {
            Propietario actualizado = propietarioService.actualizarPropietario(idPropietario, propietario);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idPropietario}")
    public ResponseEntity <?> eliminarPropietario(@PathVariable Long idPropietario) {
        try {
            propietarioService.eliminarPropietario(idPropietario);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Propietario eliminado");
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propietario no encontrado");
        }
    }



}
