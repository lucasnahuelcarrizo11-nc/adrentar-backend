package com.example.adrentar.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("INQUILINO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inquilino extends Usuario {

    @OneToMany(mappedBy = "inquilino", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("inquilino-alquileres")
    private List<Alquiler> alquileres;
}
