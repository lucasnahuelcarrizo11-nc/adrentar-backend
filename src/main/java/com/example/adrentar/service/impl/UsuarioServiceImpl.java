package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Usuario;
import com.example.adrentar.repository.UsuarioRepository;
import com.example.adrentar.service.EmailService;
import com.example.adrentar.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    @Autowired
    private EmailService emailService;


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

    public void solicitarRecuperacion(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no encontrado"));

        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        usuario.setTokenExpiracion(LocalDateTime.now().plusMinutes(30));
        usuarioRepository.save(usuario);

        emailService.enviarCorreo(
                email,
                "Recuperar contraseña - Adrentar",
                "Hacé click en el siguiente link para recuperar tu contraseña (válido 30 min):\n"
                        + "http://localhost:5173/reset-contrasenia?token=" + token
        );
    }

    public void resetContrasenia(String token, String nuevaContrasenia) {
        Usuario usuario = usuarioRepository.findByTokenRecuperacion(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token expiró");
        }

        usuario.setContrasenia(nuevaContrasenia);
        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpiracion(null);
        usuarioRepository.save(usuario);
    }

}
