package com.example.adrentar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProveedor;

    private String nombreCompleto;

    private String telefono;
    private String email;
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    private String descripcion;
    private String zona;
    private boolean activo = true;

}
