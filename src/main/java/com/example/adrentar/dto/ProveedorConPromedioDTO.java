package com.example.adrentar.dto;

import com.example.adrentar.entity.Especialidad;
import com.example.adrentar.entity.Proveedor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ProveedorConPromedioDTO {

    private Long idProveedor;
    private String nombreCompleto;
    private Especialidad especialidad;
    private String zona;
    private String telefono;
    private String email;
    private String descripcion;
    private boolean activo;
    private Double promedioPuntuacion;
    private Long cantidadResenas;

    public ProveedorConPromedioDTO(Proveedor p, Double promedioPuntuacion, Long cantidadResenas) {
        this.idProveedor = p.getIdProveedor();
        this.nombreCompleto = p.getNombreCompleto();
        this.especialidad = p.getEspecialidad();
        this.zona = p.getZona();
        this.telefono = p.getTelefono();
        this.email = p.getEmail();
        this.descripcion = p.getDescripcion();
        this.activo = p.isActivo();
        this.promedioPuntuacion = promedioPuntuacion;
        this.cantidadResenas = cantidadResenas;
    }
}
