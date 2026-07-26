package com.example.adrentar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReparacion;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    // Opcional: ayuda a identificar la reparación de un vistazo en el listado.
    // Si no lo querés, se puede sacar sin afectar el resto.
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    private LocalDateTime fecha;

    @ElementCollection
    @CollectionTable(name = "reparacion_imagenes", joinColumns = @JoinColumn(name = "id_reparacion"))
    @Column(name = "url_imagen")
    private List<String> imagenes;

    @PrePersist
    public void antesDeCrear() {
        this.fecha = LocalDateTime.now();
    }
}