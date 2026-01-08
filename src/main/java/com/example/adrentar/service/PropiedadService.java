package com.example.adrentar.service;

import com.example.adrentar.entity.Propiedad;

import java.util.List;
import java.util.Optional;

public interface PropiedadService {

    Propiedad crearPropiedad(Propiedad propiedad);

    Propiedad buscarPorDireccion(String direccion);

    List<Propiedad> listarPropiedades();

    Optional<Propiedad> obtenerPropiedadPorId(Long id);

    Propiedad actualizarPropiedad(Long idPropiedad , Propiedad propiedad);

    List<Propiedad> listarPropiedadesPorPropietario(Long propietarioId);

    void eliminarPropiedad(Long idPropiedad);
}
