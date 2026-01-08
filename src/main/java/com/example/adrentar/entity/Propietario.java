package com.example.adrentar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("PROPIETARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Propietario extends Usuario {

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // ✅ evita errores de deserialización y bucles
    private List<Propiedad> propiedades;

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Alquiler> alquileres;
}
