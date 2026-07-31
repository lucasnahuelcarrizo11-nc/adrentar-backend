package com.example.adrentar.service.impl;

import com.example.adrentar.dto.GastoPropiedadDto;
import com.example.adrentar.entity.Propiedad;
import com.example.adrentar.entity.Proveedor;
import com.example.adrentar.entity.Reparacion;
import com.example.adrentar.repository.PropiedadRepository;
import com.example.adrentar.repository.ProveedorRepository;
import com.example.adrentar.repository.ReparacionRepository;
import com.example.adrentar.service.ReparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReparacionServiceImpl implements ReparacionService {

    private static final String CARPETA_REPARACIONES = "uploads/reparaciones";

    @Autowired
    private ReparacionRepository reparacionRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Override
    public List<Reparacion> listarPorProveedor(Long idProveedor) {
        return reparacionRepository.findByProveedorIdUsuarioOrderByFechaDesc(idProveedor);
    }

    @Override
    public Reparacion crearReparacion(Long idProveedor, Long idPropiedad, String titulo, String descripcion,
                                      Double monto, MultipartFile[] imagenes) throws IOException {
        Proveedor proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Propiedad propiedad = propiedadRepository.findById(idPropiedad)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        Reparacion reparacion = new Reparacion();
        reparacion.setProveedor(proveedor);
        reparacion.setPropiedad(propiedad);
        reparacion.setTitulo(titulo);
        reparacion.setDescripcion(descripcion);
        reparacion.setMonto(monto);
        reparacion.setImagenes(guardarImagenes(imagenes));

        return reparacionRepository.save(reparacion);
    }

    @Override
    public List<GastoPropiedadDto> resumenGastos(Long idPropietario, Integer anio, Integer mes) {
        return reparacionRepository.resumenGastosPorPropietario(idPropietario, anio, mes);
    }

    public List<String> guardarImagenes(MultipartFile[] imagenes) throws IOException {
        List<String> urls = new ArrayList<>();
        if (imagenes == null) return urls;

        Path carpetaDestino = Paths.get(CARPETA_REPARACIONES);
        Files.createDirectories(carpetaDestino);

        for (MultipartFile imagen : imagenes) {
            if (imagen == null || imagen.isEmpty()) continue;

            String contentType = imagen.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Todos los archivos deben ser imágenes");
            }

            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            Path rutaDestino = carpetaDestino.resolve(nombreArchivo);
            imagen.transferTo(rutaDestino);

            urls.add("/api/reparaciones/imagenes/" + nombreArchivo);
        }

        return urls;
    }
}