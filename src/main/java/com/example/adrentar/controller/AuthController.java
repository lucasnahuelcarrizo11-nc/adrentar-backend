package com.example.adrentar.controller;

import com.example.adrentar.entity.Inquilino;
import com.example.adrentar.entity.Propietario;
import com.example.adrentar.entity.Usuario;
import com.example.adrentar.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro/propietario")
    public ResponseEntity<Usuario> registrarPropietario(@RequestBody Propietario propietario) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(propietario));
    }

    @PostMapping("/registro/inquilino")
    public ResponseEntity<Usuario> registrarInquilino(@RequestBody Inquilino inquilino) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(inquilino));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datos) {
        String email = datos.get("email");
        String contrasenia = datos.get("contrasenia");

        return usuarioService.login(email, contrasenia)
                .map(usuario -> {
                    return ResponseEntity.ok(Map.of(
                            "usuario", usuario,
                            "token", usuario.getToken(),
                            "tipo_usuario", usuario.getClass().getSimpleName()
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(401)
                        .body(Map.of("error", "Credenciales incorrectas")));
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(@RequestHeader("Authorization") String token) {
        return usuarioService.getUsuarioPorToken(token)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).body("Token inválido"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        usuarioService.logout(token);
        return ResponseEntity.ok("Sesión cerrada");
    }
}
