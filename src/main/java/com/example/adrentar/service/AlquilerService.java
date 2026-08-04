package com.example.adrentar.service;

import com.example.adrentar.dto.AlquilerCreadoDto;
import com.example.adrentar.dto.AlquilerListadoDto;
import com.example.adrentar.dto.CrearAlquilerDto;
import com.example.adrentar.dto.EditarAlquilerDto;
import com.example.adrentar.entity.Documento;

import java.util.List;
import java.util.Optional;


public interface AlquilerService {
    AlquilerCreadoDto crearAlquiler(String token, CrearAlquilerDto dto) ;

    Object obtenerMisAlquileres(String token);

    void aceptarAlquiler(String token, Long idAlquiler);

    AlquilerListadoDto editarAlquiler(String token, Long idAlquiler, EditarAlquilerDto dto);

    void rechazarAlquiler(String token, Long idAlquiler);

    void cancelarAlquiler(String token, Long idAlquiler);
}
