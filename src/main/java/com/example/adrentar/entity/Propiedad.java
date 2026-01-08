package com.example.adrentar.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPropiedad;
    @Column( nullable = false)
    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;


    @Column( nullable = false)
    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @Column( nullable = false)

    private int ambientes;

    @Column
    private Double latitud;

    @Column
    private Double longitud;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propietario")
    @JsonIgnore
    private Propietario propietario;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Alquiler> alquileres;


}

