package com.example.adrentar.service;

import com.example.adrentar.entity.Inquilino;

import java.util.List;
import java.util.Optional;

public interface InquilinoService {

    Inquilino crearInquilino(Inquilino inquilino);

    List<Inquilino> listarInquilinos();

    Optional<Inquilino> buscarInquilinoPorNombre(String nombre);

    Optional<Inquilino> buscarInquilinoPorId(Long idInquilino);

    Optional<Inquilino> buscarInquilinoPorEmail(String email);

    Inquilino actualizarInquilino(Long idInquilino,Inquilino inquilino) throws Exception;

    void eliminarInquilino(Long idInquilino) throws Exception;

}
