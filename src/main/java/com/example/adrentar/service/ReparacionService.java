package com.example.adrentar.service;

import com.example.adrentar.dto.GastoPropiedadDto;
import com.example.adrentar.entity.Reparacion;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReparacionService {

    List<Reparacion> listarPorProveedor(Long idProveedor);

    Reparacion crearReparacion(Long idProveedor, Long idPropiedad, String titulo, String descripcion,
                               Double monto, MultipartFile[] imagenes) throws IOException;

    List<String> guardarImagenes(MultipartFile[] imagenes) throws IOException;


    List<GastoPropiedadDto> resumenGastos(Long idPropietario, Integer anio, Integer mes);

}
