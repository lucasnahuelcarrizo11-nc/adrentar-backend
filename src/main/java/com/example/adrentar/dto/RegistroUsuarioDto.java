package com.example.adrentar.dto;

import com.example.adrentar.entity.TipoUsuario;
import lombok.Data;

@Data
public class RegistroUsuarioDto {
    private String nombre;
    private String apellido;
    private String email;
    private String password;

    private TipoUsuario tipoUsuario;
}
