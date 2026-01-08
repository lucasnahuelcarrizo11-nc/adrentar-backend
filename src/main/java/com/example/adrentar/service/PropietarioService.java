package com.example.adrentar.service;

import com.example.adrentar.entity.Propietario;

import java.util.List;
import java.util.Optional;

public interface PropietarioService {

    Propietario crearPropietario(Propietario propietario);

    List<Propietario> mostrarPropietarios();

    Optional<Propietario> buscarPorNombre(String nombre);

    Optional<Propietario> buscarPorId(Long idPropietario);

    Propietario actualizarPropietario(Long idPropietario, Propietario propietario) throws Exception;

    void eliminarPropietario(Long idPropietario) throws Exception;


}
