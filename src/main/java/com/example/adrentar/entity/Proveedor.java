package com.example.adrentar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor extends Usuario {


    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    private String descripcion;

    private String zona;
    private boolean activo = true;

    private String matriculaUrl;

    public Long getIdProveedor() {
        return getIdUsuario();
    }

    public void setIdProveedor(Long idProveedor) {
        setIdUsuario(idProveedor);
    }

}
