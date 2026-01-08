package com.example.adrentar.service;

import com.example.adrentar.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UsuarioService {

    Usuario registrarUsuario(Usuario usuario);

    Optional<Usuario> login(String email, String contrasenia);

    Optional<Usuario> getUsuarioPorToken(String token);

    void logout(String token);

}
