package com.example.adrentar.service;

import com.example.adrentar.entity.Reparacion;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReparacionService {

    List<Reparacion> listarPorProveedor(Long idProveedor);

    Reparacion crearReparacion(Long idProveedor, String titulo, String descripcion, MultipartFile[] imagenes) throws IOException;

    List<String> guardarImagenes(MultipartFile[] imagenes) throws IOException;
}
