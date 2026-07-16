package com.example.adrentar.service;

import com.example.adrentar.entity.Resena;

import java.util.List;

public interface ResenaService {
    Resena crearResena(Resena resena) throws Exception;
    List<Resena> listarPorProveedor(Long idProveedor);
    void eliminarResena(Long idResena) throws Exception;
    Double promedioPorProveedor(Long idProveedor);
    Long cantidadPorProveedor(Long idProveedor);
}
