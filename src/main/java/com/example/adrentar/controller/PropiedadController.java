package com.example.adrentar.controller;

import com.example.adrentar.entity.Propiedad;
import com.example.adrentar.entity.Propietario;
import com.example.adrentar.entity.Usuario;
import com.example.adrentar.service.PropiedadService;
import com.example.adrentar.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/propiedad")
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private UsuarioService usuarioService;

    // 🟢 Crear propiedad (solo propietario logueado)
    @PostMapping("/crear")
    public ResponseEntity<?> crearPropiedad(
            @RequestHeader("Authorization") String token,
            @RequestBody Propiedad propiedad) {

        // 🔥 LIMPIAR TOKEN
        String tokenLimpio = token.replace("Bearer ", "").trim();

        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorToken(tokenLimpio);

        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Propietario propietario)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Solo los propietarios pueden crear propiedades o el token es inválido.");
        }

        propiedad.setPropietario(propietario);
        Propiedad nuevaPropiedad = propiedadService.crearPropiedad(propiedad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPropiedad);
    }

    // 🔹 Listar todas las propiedades
    @GetMapping
    public ResponseEntity<List<Propiedad>> listarPropiedad() {
        List<Propiedad> propiedades = propiedadService.listarPropiedades();
        return ResponseEntity.ok(propiedades);
    }

    // 🔹 Obtener propiedad por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPropiedadById(@PathVariable Long id) {
        Optional<Propiedad> optionalPropiedad = propiedadService.obtenerPropiedadPorId(id);
        return optionalPropiedad.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propiedad no encontrada"));
    }

    // 🔹 Actualizar propiedad
    @PutMapping("/{idPropiedad}")
    public ResponseEntity<?> actualizarPropiedad(
            @PathVariable Long idPropiedad,
            @RequestBody Propiedad propiedad) {

        try {
            Propiedad actualizada = propiedadService.actualizarPropiedad(idPropiedad, propiedad);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propiedad no encontrada");
        }
    }

    // 🔹 Listar propiedades del propietario logueado
    @GetMapping("/mis-propiedades")
    public ResponseEntity<?> listarPropiedadesDelPropietario(
            @RequestHeader("Authorization") String token) {

        // 🔥 LIMPIAR TOKEN
        String tokenLimpio = token.replace("Bearer ", "").trim();

        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorToken(tokenLimpio);

        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Propietario propietario)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Solo los propietarios pueden ver sus propiedades o el token es inválido.");
        }

        List<Propiedad> propiedades = propiedadService.listarPropiedadesPorPropietario(propietario.getIdUsuario());
        return ResponseEntity.ok(propiedades);
    }

    // 🔹 Eliminar propiedad
    @DeleteMapping("/{idPropiedad}")
    public ResponseEntity<?> eliminarPropiedad(@PathVariable Long idPropiedad) {
        try {
            propiedadService.eliminarPropiedad(idPropiedad);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Propiedad eliminada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propiedad no encontrada");
        }
    }
}
