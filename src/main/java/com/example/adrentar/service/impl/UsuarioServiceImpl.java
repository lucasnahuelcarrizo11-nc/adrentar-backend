package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Usuario;
import com.example.adrentar.repository.UsuarioRepository;
import com.example.adrentar.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> login(String email, String contrasenia) {
        Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getContrasenia().equals(contrasenia)) {
            Usuario usuario = userOpt.get();
            String token = UUID.randomUUID().toString(); // ✅ token aleatorio
            usuario.setToken(token);
            usuarioRepository.save(usuario);
            usuario.setContrasenia(""); // no devolver contraseña
            return Optional.of(usuario);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> getUsuarioPorToken(String token) {
        return usuarioRepository.findByToken(token); // busca en la DB
    }

    @Override
    public void logout(String token) {
        usuarioRepository.findByToken(token).ifPresent(u -> {
            u.setToken(null);
            usuarioRepository.save(u);
        });
    }
}
